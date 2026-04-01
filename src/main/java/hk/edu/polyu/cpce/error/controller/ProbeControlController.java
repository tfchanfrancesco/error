package hk.edu.polyu.cpce.error.controller;

import hk.edu.polyu.cpce.error.util.EspStringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 仅用于在 OpenShift / K8s 环境测试探针：通过 {@link AvailabilityChangeEvent} 切换
 * {@code /actuator/health/liveness} 与 {@code /actuator/health/readiness} 的聚合结果。
 * <p>
 * 若配置了 {@code app.probe-control.token}，请求须带请求头 {@code X-Probe-Control-Token}。
 */
@RestController
@RequestMapping("/api/probe")
public class ProbeControlController {

	private final ApplicationContext applicationContext;
	private final ApplicationAvailability applicationAvailability;

	@Value("${app.probe-control.token:}")
	private String probeControlToken;

	public ProbeControlController(ApplicationContext applicationContext, ApplicationAvailability applicationAvailability) {
		this.applicationContext = applicationContext;
		this.applicationAvailability = applicationAvailability;
	}

	@RequestMapping(value = "/readiness/accept", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> readinessAccept(HttpServletRequest request) {
		assertAuthorized(request);
		AvailabilityChangeEvent.publish(applicationContext, ReadinessState.ACCEPTING_TRAFFIC);
		return ResponseEntity.ok(statusBody("readiness", ReadinessState.ACCEPTING_TRAFFIC.name()));
	}

	@RequestMapping(value = "/readiness/refuse", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> readinessRefuse(HttpServletRequest request) {
		assertAuthorized(request);
		AvailabilityChangeEvent.publish(applicationContext, ReadinessState.REFUSING_TRAFFIC);
		return ResponseEntity.ok(statusBody("readiness", ReadinessState.REFUSING_TRAFFIC.name()));
	}

	@RequestMapping(value = "/liveness/correct", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> livenessCorrect(HttpServletRequest request) {
		assertAuthorized(request);
		AvailabilityChangeEvent.publish(applicationContext, LivenessState.CORRECT);
		return ResponseEntity.ok(statusBody("liveness", LivenessState.CORRECT.name()));
	}

	@RequestMapping(value = "/liveness/broken", method = {RequestMethod.GET, RequestMethod.POST})
	public ResponseEntity<Map<String, Object>> livenessBroken(HttpServletRequest request) {
		assertAuthorized(request);
		AvailabilityChangeEvent.publish(applicationContext, LivenessState.BROKEN);
		return ResponseEntity.ok(statusBody("liveness", LivenessState.BROKEN.name()));
	}

	@GetMapping("/status")
	public Map<String, Object> status(HttpServletRequest request) {
		assertAuthorized(request);
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("liveness", String.valueOf(applicationAvailability.getLivenessState()));
		body.put("readiness", String.valueOf(applicationAvailability.getReadinessState()));
		body.put("healthLivenessPath", "/actuator/health/liveness");
		body.put("healthReadinessPath", "/actuator/health/readiness");
		return body;
	}

	private void assertAuthorized(HttpServletRequest request) {
		if (EspStringUtils.isBlank(probeControlToken)) {
			return;
		}
		String header = request.getHeader("X-Probe-Control-Token");
		if (!probeControlToken.equals(header)) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid or missing X-Probe-Control-Token");
		}
	}

	private static Map<String, Object> statusBody(String key, String value) {
		Map<String, Object> m = new LinkedHashMap<>();
		m.put(key, value);
		m.put("healthLivenessPath", "/actuator/health/liveness");
		m.put("healthReadinessPath", "/actuator/health/readiness");
		return m;
	}
}
