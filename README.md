# iSleepOnTime
## Developing an Android application for behavior change in bedtime procrastination
 
The main code can be found under `app/src/main/java/applab/bedtimeapp/`
 
| File | |
|---|---|
| db | Folder containing database related classes
| - DatabaseHelper.java | Main DB manager
| - JSONHelper.java | SQLite to JSON converter
| - ResultOperations.java | CRUD operations for local DB
| model | Folder containing model class for database entries
| - Result.java | DB persistent class
| utils | Folder containing utility scripts
| - AlarmReceiver.java | Receiving alarm intent and starting AlarmSnoozActivity
| - Constants.java | Class for global constant numbers, strings
| - NotificationHelper.java | Notification scheduler
| - NotificationPublisher.java | Receiving notification intent and publishing the notification
| - RebootReceiver.java | Receiver waiting for device reboot, will start RebootService
| - RebootService.java | Class responsible for the service jobs, like setting notifications and alarms again, after reboot
| - RestClient.java | Sending data to server
| - TextThumbSeekBar.java | Label boats according to boat clusters
| - utils.java | Class for main generic utils (string manipulations, date-time calculations)
| AlarmDrawerActivity.java | Screen for setting the bedtime and alarm time
| AlarmFeedbackActivity.java | Screen when alarm was switched off
| AlarmSnoozeActivity.java | Screen when alarm rings
| CoachActivity.java | Screen with coaching owl
| FragmentSlide.java | Fragment for tutorial slides
| MainDrawerActivity.java | Main screen (homescreen), current DB and SharedPreferences are retrieved, missed notifications are checked, possibly redirecting to homescreen chosen by user (AlarmDrawerActivity, ProgressDrawerActivity)
| ProgressDrawerActivity.java | Screen with graphs on progress over last 7 days
| QuestionnaireActivity.java | Screen with daily questionnaire
| ReasonsActivity.java | Screen with duration and reason for bedtime procrastination
| SelfEfficacyActivity.java | Screen with questionnaire about self-efficacy
| SettingsDrawerActivity.java | Screen for changing the app’s settings
| TimePickerFragment.java | Logic when choosing a time through TimePicker (e.g. setting alarm)
| TutorialIntro.java | Tutorial slides on first start, also contains DB setup
| UserIDActivity.java | Screen for entering the User ID for the study