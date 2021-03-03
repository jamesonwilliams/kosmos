# kosmos

Kosmos is a lightweight Android utility for authenticating your users via [Amazon Cognito](https://aws.amazon.com/cognito/).

Amazon Cognito can be thought of as the "glue" between your application users and the roles/policies in your AWS account.

Kosmos provides a `signUp` API to register new users into a [User Pool](https://aws.amazon.com/premiumsupport/knowledge-center/cognito-user-pools-identity-pools/). Registered users can then `signIn` using Cognito's [Secure Remote Password (SRP)](https://en.wikipedia.org/wiki/Secure_Remote_Password_protocol) protocol. After signing in, [Cognito grants auth tokens](https://docs.aws.amazon.com/cognito/latest/developerguide/amazon-cognito-user-pools-using-tokens-with-identity-providers.html), which can be used to authenticate requests to other Amazon services.

Kosmos stored credentials so you can use them in your API calls. Credentials are stored _securely_ using [Android Jetpack's Security library](https://developer.android.com/jetpack/androidx/releases/security). Depending on your security posture, or the level of API support you need, you can also implement your own `CredentialStorage`.
