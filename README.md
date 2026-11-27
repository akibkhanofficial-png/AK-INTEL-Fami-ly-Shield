# AK INTEL – Family Shield

Android child-device prototype. Permissions are enabled by the device user in Android Settings.

## Implemented in this prototype
- Local daily app-usage totals using Android Usage Access.
- User-started location collection with a persistent Android foreground notification; last coordinates are stored locally on the device.
- Optional WhatsApp/Instagram notification app + visible title label only. Message bodies and chats are not read.

## Important limitations
- No remote parent dashboard, account pairing, cloud sync, geofencing alerts, or multi-child management yet. Location coordinates are NOT uploaded anywhere.
- iOS app is not included; it requires a separate native app and Apple's approved Family Controls APIs.
- Notification titles may be hidden, generic, grouped, or unavailable; they cannot reliably identify every sender.
- Usage Access and location permissions must be explicitly enabled on each child device and can be revoked.
- No covert camera/microphone access. Any future sharing must be visible and explicitly approved.
- This is a development prototype, not a finished parental-control product. Test on-device before relying on it.

## Build
Open the repository Actions tab and run the Android debug APK workflow. Download the APK artifact after a successful build.
