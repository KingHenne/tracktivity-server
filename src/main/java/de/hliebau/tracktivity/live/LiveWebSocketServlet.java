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

import de.hliebau.tracktivity.service.ActivityService;

public class LiveWebSocketServlet extends WebSocketServlet {

	private final class ActivityMessageInbound extends MessageInbound {

		private final int id;

		private final Logger logger = LoggerFactory.getLogger(this.getClass());

		private final Principal principal;

		public ActivityMessageInbound(int id, Principal principal) {
			this.id = id;
			this.principal = principal;
		}

		private void broadcast(String message) {
			for (ActivityMessageInbound connection : connections) {
				try {
					logger.debug("broadcasting message from {} to {}: {}", id, connection.id, message);
					CharBuffer buffer = CharBuffer.wrap(message);
					connection.getWsOutbound().writeTextMessage(buffer);
				} catch (IOException e) {
					// Ignore
					e.printStackTrace();
				}
			}
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
		protected void onTextMessage(CharBuffer textMessage) {
			// TODO: only process messages from logged in users
			// if (this.principal != null)
			logger.debug("trying to convert message: {}", textMessage.toString());
			ObjectMapper mapper = new ObjectMapper();
			try {
				WebSocketMessage message = mapper.readValue(textMessage.toString(), WebSocketMessage.class);
				// TODO: read and store transmitted tracking info
				// TODO: broadcast new info to all non-tracking connections
				broadcast(message.getData());
			} catch (JsonParseException e) {
				e.printStackTrace();
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private static final long serialVersionUID = -5397866901866887147L;

	@Autowired
	private ActivityService activityService;

	private final AtomicInteger connectionIds = new AtomicInteger(0);

	private final Set<ActivityMessageInbound> connections = new CopyOnWriteArraySet<ActivityMessageInbound>();

	@Override
	protected StreamInbound createWebSocketInbound(String subProtocol, HttpServletRequest request) {
		return new ActivityMessageInbound(connectionIds.incrementAndGet(), request.getUserPrincipal());
	}

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
	}

}
