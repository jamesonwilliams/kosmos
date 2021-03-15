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

### User registration flow

1. Collect email and password from user, and call `registerUser(...)`.
2. Amazon Cognito will send a verification email to the user.
3. Prompt the user to enter the verification code in your UI, and pass it to `confirmRegistration(...)`.
4. If the code matches, registration succeeds and the user can proceed to sign in.

### Signing in
To sign in, simply call `signIn(...)`.

Once a user has signed in, OAuth tokens are available through `tokens()`. A `ValidTokens` instance contains an OIDC ID token and an OAuth2 access token. Session-scoped AWS credentials are _also_ available, through `session()`. An `AuthenticatedSession` will contain an AWS IAM access key, secret key, and session token. (Under the hood, Cognito has called `AssumeRole`, to temporarily use one of the roles you've defined.)

## Demo App

Be sure to checkout the [demo app](./demo-app).

It shows user sign-up:

<img src="https://user-images.githubusercontent.com/899569/109924184-86438a80-7c85-11eb-8e07-b178f0517eb4.gif" width="200">

Code confirmation and sign-in:

<img src="https://user-images.githubusercontent.com/899569/109924213-8fccf280-7c85-11eb-9967-6098c9dae9e1.gif" width="200">
