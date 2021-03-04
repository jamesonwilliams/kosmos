<a name="kosmos-the-ghost"><img alt="Kosmos the Ghost" src="https://user-images.githubusercontent.com/899569/109872251-c9740e00-7c31-11eb-8bab-f57163b43ff9.jpg" width="200"></a>

# kosmos

Kosmos is a lightweight Android library to authenticate your users with [Amazon Cognito](https://aws.amazon.com/cognito/).

Amazon Cognito maps your app's user accounts to roles/policies in your AWS account. Registered users can `signIn` to obtain [auth tokens](https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-tokens-with-identity-providers.html), useful for making authenticated requests to other Amazon services.

Kosmos can help you to register new user accounts, and to authenticate existing accounts. Under the hood, Kosmos handles a number of details on your behalf, including [Secure Remote Password (SRP)](https://en.wikipedia.org/wiki/Secure_Remote_Password_protocol) exchange, secure token storage, and token refreshes.

To register a new user:
1. Collect email and password from user, and call `registerUser(...)`.
2. Amazon Cognito will send a verification email to the user.
3. Collect the verification code in your UI, and call `confirmRegistration(...)`.
4. If the code matches, the user can proceed to sign in.

To sign in, simply call `signIn(...)`. Once a user has signed in, they can access credentials through `session()`. A `ValidSession` contains an OIDC ID token and an OAuth2 access token.

## Demo App

Checkout the [demo app](./demo-app).

It shows user sign-up:

<img src="https://user-images.githubusercontent.com/899569/109924184-86438a80-7c85-11eb-8e07-b178f0517eb4.gif" width="200">

Code confirmation and sign-in:

<img src="https://user-images.githubusercontent.com/899569/109924213-8fccf280-7c85-11eb-9967-6098c9dae9e1.gif" width="200">
