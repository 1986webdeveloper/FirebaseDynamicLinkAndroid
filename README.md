# FirebaseDynamicLinkAndroid
While using e-commerce apps, we have seen sharing short links which ultimately opens in a particular screen of the app rather than the launcher screen. Like for eg, link generated while sharing a post will ultimately open on that particular post itself.

This project depicts use of Firebase Dynamic Links which can be shared among application users that can then be received in the particular screen. Along with shortening, we can also pass parameters (to go to a particular post, we need post id, user id, etc). We can pass as many parameters as needed.

To use the code, we need to setup firebase with our package name and download json file from firebase console. We can do it manually or firebase assistant can do this for us. To make firebase assistant do this for us, follow the steps in android studio:

Step 1: Tools -> Firebase
This step will open firebase assistant (on the right side usually)

Step 2: Click on Dynamic Links -> click on "add a dynamic link"

Step 3: Click on the first step (connect to firebase)

A dialog will be opened, click on "create new project", alter the name if required.

(Note: Delete existing google-services.json file before connecting to firebase)

Step 4: Go to firebase console -> your project ->Dynamic Link -> Get Started -> (here we will be asked to enter some string for appcode; enter any string that best suits your project (eg., company domain); this string will appear in the short link generated.) 

Step 5: Replace R.string.appcode with your appcode (we can find appcode in firebase console -> my project -> Dynamic link -> top left corner (xxxx.page.link))


Set up is ready. 

Tools Used:
- Android Studio 3.0.0

Firebase Version Used:
- firebase-core:16.0.0
- firebase-dynamic-links:16.0.0
