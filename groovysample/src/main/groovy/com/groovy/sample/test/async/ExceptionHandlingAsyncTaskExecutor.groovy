package com.groovy.sample.test.async

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.task.AsyncTaskExecutor;

class ExceptionHandlingAsyncTaskExecutor  implements AsyncTaskExecutor,
InitializingBean, DisposableBean {

	final Logger log = LoggerFactory.getLogger(ExceptionHandlingAsyncTaskExecutor.class);

	final AsyncTaskExecutor executor;

	public ExceptionHandlingAsyncTaskExecutor(AsyncTaskExecutor executor) {
		this.executor = executor;
	}

	@Override
	void execute(Runnable task) {
		executor.execute(task);
	}

	@Override
	void execute(Runnable task, long startTimeout) {
		executor.execute(createWrappedRunnable(task), startTimeout);
	}

	private <T> Callable<T> createCallable(final Callable<T> task) {
		def caller = {
			try {
				task.call();
			} catch (Exception e) {
				handle(e);
				throw e;
			}
		};
	}

	private Runnable createWrappedRunnable(final Runnable task) {
		def wrapper = {
			try {
				task.run();
			} catch (Exception e) {
				handle(e);
			}
		};
	}

	protected void handle(Exception e) {
		log.error("Caught async exception", e);
	}

	@Override
	Future<?> submit(Runnable task) {
		executor.submit(createWrappedRunnable(task));
	}

	@Override
	<T> Future<T> submit(Callable<T> task) {
		executor.submit(createCallable(task));
	}

	@Override
	void destroy() throws Exception {
		if (executor instanceof DisposableBean) {
			DisposableBean bean = (DisposableBean) executor;
			bean.destroy();
		}
	}

	@Override
	void afterPropertiesSet() throws Exception {
		if (executor instanceof InitializingBean) {
			InitializingBean bean = (InitializingBean) executor;
			bean.afterPropertiesSet();
		}
	}
}
