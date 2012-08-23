package de.hliebau.tracktivity.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class DebugHelper {

	@Pointcut("execution(* de.hliebau.tracktivity.util.GpxParser.createTrack(..))")
	public void gpxparsing() {
	}

	@Around("gpxparsing()")
	public Object logExecutionDuration(ProceedingJoinPoint joinpoint) throws Throwable {
		String className = joinpoint.getSignature().getDeclaringType().getCanonicalName();
		String methodName = joinpoint.getSignature().getName();
		Logger logger = LoggerFactory.getLogger(className);
		long startTime = System.currentTimeMillis();
		try {
			return joinpoint.proceed(joinpoint.getArgs());
		} finally {
			long endTime = System.currentTimeMillis();
			double millis = endTime - startTime;
			logger.debug("Finished execution of " + methodName + " in " + millis + " milliseconds.");
		}
	}

}
