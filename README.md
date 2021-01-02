# Change of behavior on failure with the ktor client

The test program is [Main.kt](src/main/kotlin/Main.kt).

The program makes two requests: the first is valid and the second is invalid.

If you run it with the ktor client 1.4.3 you get:

```
19:01:38.476 INFO  [ApplicationEngineEnvironmentReloading.kt:135] - No ktor.deployment.watch patterns specified, automatic reload is not active [DefaultDispatcher-worker-2]
19:01:38.525 INFO  [BaseApplicationEngine.kt:42] - Responding at http://0.0.0.0:8080 [DefaultDispatcher-worker-2]
/good: 200 OK
/bad: 404 Not Found
```

<br>
However, if you change the ktor jar to 1.5.0 in `gradle.properties` you get:

```
19:03:03.678 INFO  [ApplicationEngineEnvironmentReloading.kt:148] - Autoreload is disabled because the development mode is off. [DefaultDispatcher-worker-1]
19:03:03.737 INFO  [BaseApplicationEngine.kt:42] - Responding at http://0.0.0.0:8080 [DefaultDispatcher-worker-1]
/good: 200 OK
Exception in thread "main" io.ktor.client.features.ClientRequestException: Client request(http://0.0.0.0:8080/bad) invalid: 404 Not Found. Text: ""
	at io.ktor.client.features.DefaultResponseValidationKt$addDefaultResponseValidation$1$1.invokeSuspend(DefaultResponseValidation.kt:38)
	at io.ktor.client.features.DefaultResponseValidationKt$addDefaultResponseValidation$1$1.invoke(DefaultResponseValidation.kt)
	at io.ktor.client.features.HttpCallValidator.validateResponse(HttpCallValidator.kt:54)
	at io.ktor.client.features.HttpCallValidator$Companion$install$3.invokeSuspend(HttpCallValidator.kt:129)
	at io.ktor.client.features.HttpCallValidator$Companion$install$3.invoke(HttpCallValidator.kt)
	at io.ktor.client.features.HttpSend$Feature$install$1.invokeSuspend(HttpSend.kt:99)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:33)
	at io.ktor.util.pipeline.SuspendFunctionGun.resumeRootWith(SuspendFunctionGun.kt:188)
	at io.ktor.util.pipeline.SuspendFunctionGun.loop(SuspendFunctionGun.kt:144)
	at io.ktor.util.pipeline.SuspendFunctionGun.access$loop(SuspendFunctionGun.kt:15)
	at io.ktor.util.pipeline.SuspendFunctionGun$continuation$1.resumeWith(SuspendFunctionGun.kt:90)
	at kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(ContinuationImpl.kt:46)
	at kotlinx.coroutines.DispatchedTask.run(DispatchedTask.kt:106)
	at kotlinx.coroutines.EventLoopImplBase.processNextEvent(EventLoop.common.kt:274)
	at kotlinx.coroutines.BlockingCoroutine.joinBlocking(Builders.kt:84)
	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking(Builders.kt:59)
	at kotlinx.coroutines.BuildersKt.runBlocking(Unknown Source)
	at kotlinx.coroutines.BuildersKt__BuildersKt.runBlocking$default(Builders.kt:38)
	at kotlinx.coroutines.BuildersKt.runBlocking$default(Unknown Source)
	at Main.main(Main.kt:46)
```

<br>
As you can see, the 1.5.0 jar throws an exception rather than reporting the failure via the `response.status` value. The
1.5.0 release notes do not suggest this change in behavior is intentional.