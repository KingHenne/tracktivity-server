package de.hliebau.tracktivity.live;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.security.Principal;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hliebau.tracktivity.domain.Activity;
import de.hliebau.tracktivity.domain.ActivityType;
import de.hliebau.tracktivity.domain.Track;
import de.hliebau.tracktivity.domain.TrackPoint;
import de.hliebau.tracktivity.domain.TrackSegment;
import de.hliebau.tracktivity.domain.User;
import de.hliebau.tracktivity.service.ActivityService;
import de.hliebau.tracktivity.service.UserService;

public class LiveWebSocketServlet extends WebSocketServlet {

	private final class ActivityMessageInbound extends MessageInbound {

		private final int id;

		private final Logger logger = LoggerFactory.getLogger(this.getClass());

		private final ObjectMapper mapper = new ObjectMapper();

		private Activity recordingActivity;

		private boolean recordingPaused = false;

		private final User user;

		public ActivityMessageInbound(int id, User user) {
			this.id = id;
			this.user = user;
		}

		private void broadcast(String message, boolean includeSender) {
			for (ActivityMessageInbound connection : connections) {
				if (connection.equals(this) && !includeSender) {
					continue;
				}
				try {
					logger.debug("broadcasting message from {} to {}: {}", id, connection.id, message);
					CharBuffer buffer = CharBuffer.wrap(message);
					connection.getWsOutbound().writeTextMessage(buffer);
				} catch (IOException e) {
					logger.warn("Could not broadcast message from {} to {}.", id, connection.id);
				}
			}
		}

		protected Activity createLiveActivity(ActivityType type) {
			this.recordingActivity = new Activity(new Track(new TrackSegment()));
			this.recordingActivity.setRecording(true);
			this.recordingActivity.setUser(user);
			this.recordingActivity.setType(type);
			// activityService.createActivity(this.recordingActivity);
			return this.recordingActivity;
		}

		@Override
		protected void onBinaryMessage(ByteBuffer message) throws IOException {
			throw new UnsupportedOperationException("Only text messages are supported.");
		}

		@Override
		protected void onClose(int status) {
			connections.remove(this);
			logger.debug("connection (id={}) closed", id);
		}

		@Override
		protected void onOpen(WsOutbound outbound) {
			connections.add(this);
			logger.debug("new connection (id={})", id);
		}

		@Override
		protected void onTextMessage(CharBuffer textMessage) throws JsonParseException, JsonMappingException,
				IOException {
			if (this.user == null) {
				logger.warn("received a message from an unauthorized user");
				return;
			}

			logger.debug("received message from {}: {}", this.user.getUsername(), textMessage.toString());
			WebSocketMessage message = this.mapper.readValue(textMessage.toString(), WebSocketMessage.class);
			message.setUsername(this.user.getUsername());

			switch (message.getEvent()) {
			case STARTED:
				// create a new activity and return the ID to the sender
				this.createLiveActivity(message.getActivityType());
				// long activityId =
				// this.createLiveActivity(message.getActivityType()).getId();
				// String response = String.format("{ id: %d }", activityId);
				// this.getWsOutbound().writeTextMessage(CharBuffer.wrap(response));
				break;
			case RECORDING:
				TrackPoint newPoint = message.getPoint();
				if (this.recordingActivity == null) {
					this.createLiveActivity(message.getActivityType());
				}
				if (this.recordingPaused) {
					// create a new segment after a pause
					this.recordingPaused = false;
					this.recordingActivity.getTrack().addSegment(new TrackSegment());
				}
				// finally add the recorded point and persist the update
				this.recordingActivity.addPoint(newPoint);
				// activityService.updateActivity(this.recordingActivity);
				break;
			case PAUSED:
				this.recordingPaused = true;
				break;
			case FINISHED:
				this.recordingActivity.setRecording(false);
				// activityService.updateActivity(this.recordingActivity);
				this.recordingActivity = null;
				break;
			}

			// broadcast the (modified) incoming message to all listening
			// connections
			broadcast(this.mapper.writeValueAsString(message), false);
		}

	}

	private static final long serialVersionUID = -5397866901866887147L;

	@Autowired
	private ActivityService activityService;

	private final AtomicInteger connectionIds = new AtomicInteger(0);

	private final Set<ActivityMessageInbound> connections = new CopyOnWriteArraySet<ActivityMessageInbound>();

	@Autowired
	private UserService userService;

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
		Principal userPrincipal = request.getUserPrincipal();
		User user = null;
		if (userPrincipal != null) {
			user = userService.retrieveUser(userPrincipal.getName(), false);
		}
		return new ActivityMessageInbound(connectionIds.incrementAndGet(), user);
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

}
