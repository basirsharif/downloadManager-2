# Download Manager
Demo app Download single/multiple files using Download Manager in Android

Download Manager is a system service which allows to handle long-running HTTP downloads in the background and notify the triggering application via a broadcast receiver once the download is finished. It was introduced in Android 2.3. (API 9).

Download manager helps use reduce the burden of writing our own download service with all those error handling. In this tutorial we will see how to create a download manager to download a file in background and notify the application once the file is downloaded.

# Single Download File
We are passing a request to the Download Manager with the download file URL, Title and Description to be shown in notification bar etc. We can also specify whether the download should happen over wifi or cellular or both.

# Multiple file Downloads
Downloading multiple files is easy. just need to add the above request in a loop. The download requests will be added into the queue.
I am using a single download url in the loop. You may use multiple urls depending on your requirements by adding it in a list and iterating it.

# Listening to Download Complete
You may listen when a download is completed and perform some actions like showing a download completed notification. In order to do that you need to keep a track of the  refid  that we get while enqueuing the download requests. After every enqueue add the refid into an Array list.
