package com.wasseemb.FeatherForReddit

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class RetryWithDelay(private val maxRetries: Int,
    private val retryDelaySeconds: Int) : io.reactivex.functions.Function<Observable<out Throwable>, Observable<*>> {
  private var retryCount: Int = 0

  override fun apply(attempts: Observable<out Throwable>): Observable<*> {
    return attempts
        .flatMap { throwable ->
          if (++retryCount < maxRetries) {
            // When this Observable calls onNext, the original
            // Observable will be retried (i.e. re-subscribed).
            Observable.timer(retryDelaySeconds.toLong(),
                TimeUnit.SECONDS)
          } else Observable.error(throwable)

          // Max retries hit. Just pass the error along.
        }
  }
}



