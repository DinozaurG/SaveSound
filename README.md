Android application “SaveSound”

Laboratory Project №1
Deadline - 23:59 29.09.2019


It is necessary to develop mobile application for audio files recording.

Minimum Android version support - Android 6.0.

GUI Design 
https://www.figma.com/file/AEj3Utvp7lm7i45Z5iMWuN/Untitled?node-id=0%3A1

Design Requirements
The application should be implemented as close to the layout as possible.
You need to configure the application icon (you need to come up with a design yourself).
It is necessary to design the Launch Screen. An empty Launch Screen will not be evaluated.

Main Application Screens

Launch Screen 
When you start the application, you must display the Launch Screen for 2 seconds. Then the main screen automatically opens.

Main Screen
The main screen displays a list of previously recorded audio files. If there are no audio files, it is necessary to display the corresponding image-placeholder and the label “You have not recorded any audio yet. Use the button below. ”
On the main screen, you must implement the ability to search for an audio file by name. When you click on the magnifying glass icon, you need to search. If a value is entered in the search bar, it is necessary to display the delete icon, when clicked, it is necessary to clear the search bar and display all the audio files in the table.
You must realize the ability to delete audio files using Swipe-to-delete. The corresponding audio file must be deleted from the device’s memory. If the deletion is successful, you must display Toast with the text “The file was deleted successfully”.
When you click on the record button, it is necessary to smoothly display the recording panel from the bottom up (with animation). On this panel, you need to start the timer, which will stop when recording stops.
When you click on the “Pause” button, you must stop recording, stop the timer and display a pop-up window for entering the name of the audio recording. The name is required. When you click on the Cancel button, it is necessary to close the recording panel gently from top to bottom. If you successfully save the audio file in memory, you must display Toast with the text “Audio was saved successfully”. The corresponding entry should appear in the table.
When you click on the “Play” button in the table cell, you need to play the audio file. When moving the slider, you need to rewind the audio file. In the table cell you need to display the current recording time and the total length of the audio.

Support for various resolutions and screen orientations
The application must support landscape and portrait orientation. All elements should be visible in any orientation of the screen.
The application must support smartphones and tablets. A tablet app layout is included.

Other
Recordable audio files must be stored in the file system of the device.
The application should work correctly with Permissions (request, notify about the lack of permission).
In case of errors, it is necessary to display a pop-up window with the error text.
When implementing, you must follow the Model-View-Controller architectural approach.
When implementing, you must comply with the Kotlin Coding Convention
 https://kotlinlang.org/docs/reference/coding-conventions.html
