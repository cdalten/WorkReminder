# WorkReminder - This is something I wrote for work. The Android phone app automatically logins into my <s>idiot</s> employer's website and gets the current schedule. And based on the hours set by the gestapo, it will automatically set the alarm on my phone 20 minutes before the start of my shift. 

# Some other things it can do is that it only checks for the new schedule once a week. I do this to save bandwidth.  Also because my phone carrier sucks dick by randomly dropping my signal. In addition I can change the alarm notification via a preference GUI and override my schedule in the event that I want to start earlier, later, or that I don't want to work at all. The latter is meant for those days when it's really nice outside -). 

# Now, as for the coding itself. Some it was automatically generated by the IDE and some of it was copy and pasted from the Android docs. And part of the time algorithm was taken from an elementary grade school book. More to the point, I translated the steps found in this book into the corresponding algorithm since your typical 4th grade math/science book doesn't talk about computer algorithms.

# --------
# Now for an outline of the code.
AlarmIntentService - Used to handle when I hit snooze of dismiss on my alarm. <br>
AlarmNotificationMainAcitivy - Handle the notification if the app is destoryed. <br>
AlarmRingTone - Have the notification make noise when the alarm goes off. <br>
AlarmTimer - Calculate the new time based on the minutes before the shift. <br> 
ConnectionCallback - Used to handle should the wifi lose its signal. <br>
CurrentWeekSchedule - Display and possibly update the schedule list because HTML won't work. <br>
dayNotification - set the Nofication based on the the day and the current time. <br>
GlobalNotificationBuilder - Memory management stuff. <br>
HourFormat - This autofills and updates the menu with the shift the person clicked on. <br>
Logout - Logout the user. <br>
MainActivity - Used to set up the web page display. <br>
Military Time - Convert to military time. Needed for time comparisons. <br>
RememberMe - Save my name and password. <br>
StoreHoursInGUI - Have the current schedule stored in a list. Needed to display as list <br>
WorkNotificationReceiver - Sets the notification alarm for when I have to work. <br>
WorkNetworkFragment - Handle when I lose my wifi signal. <br>
WorkPreferences - Save how many minutes before my shift should the alarm go off. <br>
WorkReaderContract - For global variables that the resource file can't handle. <br>
