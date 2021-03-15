[![Release](https://jitpack.io/v/jamesonwilliams/kosmos.svg)](https://jitpack.io/#jamesonwilliams/kosmos)

<a name="kosmos-the-ghost"><img alt="Kosmos the Ghost" src="https://user-images.githubusercontent.com/899569/109872251-c9740e00-7c31-11eb-8bab-f57163b43ff9.jpg" width="200"></a>

# kosmos

Kosmos is a lightweight Android library for authorizing your app's users to access resources in your AWS account.

## AWS IAM, Amazon Cognito

Access to AWS resources is traditionally controlled through [AWS' Identity & Access Management](https://aws.amazon.com/iam/) service.  However, your app's users like to sign-in with their own username and password, not with your organization's IAM credentials. That's one of the problems [Amazon Cognito](https://aws.amazon.com/cognito/) solves.

Cognito enables users to register accounts and sign into your app. User accounts are mapped to roles/policies which you define.  Registered users can `signIn(...)` to obtain credentials. Credentials can be [OAuth2 tokens](https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-tokens-with-identity-providers.html), or temporary IAM credentials, allowing access to specific resources in your account.

## Kosmos' Design

Under the hood, Kosmos includes a few barebones REST clients to communicate with Amazon Cognito endpoints. It also provides a high-level client for performing common application use cases (signin, account registration, etc.)

The library handles a number of low-level details for you, such as exchanging challenges via the [Secure Remote Password (SRP) Protocol](https://en.wikipedia.org/wiki/Secure_Remote_Password_protocol), securely storing credentials, and automatically refreshing expired credentials.

## How to Use It

### Setup Backend Resources
Firstly, you need to [setup an Amazon Cognito User Pool](https://docs.aws.amazon.com/cognito/latest/developerguide/tutorial-create-user-pool.html). Go to the Amazon Cognito Console, and [create a User Pool](https://console.aws.amazon.com/cognito/users/?region=us-east-1#/pool/new/create). Be sure to add an app and make note of its ID and seret.

Next, create an [Amazon Cognito Identity Pool](https://docs.aws.amazon.com/cognito/latest/developerguide/tutorial-create-identity-pool.html). You can do so through [the Amazon Cognito Console, here](https://console.aws.amazon.com/cognito/create/). When prompted to add provider information, enter the ID of the User Pool you created above, as well as the app client ID, from above.

### Installation

The library is [available from JitPack](https://jitpack.io/#jamesonwilliams/kosmos/0.1).

To start using it, add the following to your root-level `build.gradle`:

```gradle
allprojects {
    repositories {
        // Add this line:
        maven { url 'https://jitpack.io' }
    }
}
```

And take a dependency on Kosmos in your application's `build.gradle`:
```gradle
dependencies {
    implementation 'com.github.jamesonwilliams:kosmos:0.1'
}
```

Instantiate an instance of `Auth`:
```kotlin
val auth = Auth(
    context = applicationContext,
    userPoolId = 'your cognito user pool id',
    identityPoolId = 'your cognito identity pool id',
    clientId = 'your cognito client app id',
    clientSecret = 'your cognito client app secret'
)
```

### User registration flow

Firstly, collect the user's desired email and password, and call `registerUser(...)`. Amazon Cognito will send a verification email to the user.

```kotlin
lifcycleScope.launch(Dispatchers.IO) {
    when (auth.registerUser(email, pass, mapOf("email" to email))) {
        is ConfirmedRegistration -> { /* goto sign in */ }
        is UnconfirmedRegistration -> { /* prompt user for code */ }
    }
}
```
When it does, prompt the user to enter the verification code in your UI, and pass it to `confirmRegistration(...)`.

```kotlin
lifecycleScope.launch(Dispatchers.IO) {
    auth.confirmSignIn(username, code)
}
```

If the code matches, registration succeeds and the user can proceed to sign in.

### Signing in
To sign in, simply call `signIn(...)`:
```kotlin
lifecycleScope.launch(Dispatchers.IO) {
    auth.signIn(username, password)
}
```

### Accessing tokens and credentials

Once a user has signed in, OAuth tokens are available through `tokens()`. A `ValidTokens` instance contains an OIDC ID token and an OAuth2 access token.

```kotlin
// Get OAuth2 access token and OIDC ID token
lifecycleScope.launch(Dispatchers.IO) {
    when (val tokens = auth.tokens()) {
        is ValidTokens -> {
            val accessToken = tokens.accessToken
            val idToken = tokens.idToken
        }
    }
}
```

Session-scoped AWS credentials are _also_ available, through `session()`. An `AuthenticatedSession` will contain an AWS IAM access key, secret key, and session token.

```kotlin
// Call session() to get AWS IAM session credentials
// You can use these to call AWS services
lifecycleScope.launch(Dispatchers.IO) {
    when (val session = auth.session()) {
        is AuthenticatedSession -> {
            val accessKey = session.credentials.accessKey
            val secretKey = session.credentials.secretKey
            val sessionToken = session.credentials.sessionToken
        }
        else -> { /* goto sign in flow */ }
    }
}
```

## Demo App

Checkout the [demo app](./demo-app), included in this repo.

It shows user sign-up:

<img src="https://user-images.githubusercontent.com/899569/109924184-86438a80-7c85-11eb-8e07-b178f0517eb4.gif" width="200">

Code confirmation and sign-in:

<img src="https://user-images.githubusercontent.com/899569/109924213-8fccf280-7c85-11eb-9967-6098c9dae9e1.gif" width="200">
