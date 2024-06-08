package me.hchome.example.config.async;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author Cliff Pan
 * @since
 */
@Async
@Configuration
public class AsyncConfig implements AsyncConfigurer {


	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		int coreSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
		executor.setCorePoolSize(coreSize);
		executor.setQueueCapacity(2000);
		executor.setKeepAliveSeconds(60);
		executor.afterPropertiesSet();
		return new DelegatingSecurityContextAsyncTaskExecutor(executor);
	}

}
