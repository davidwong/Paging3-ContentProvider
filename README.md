# Paging 3 for Content Provider

This is demo app for using the Android Paging Library 3.0 to access data from a content provider.

When run it will access the Telephony content provider to load SMS messages into a list with pagination.

#### RxJava2, too

The app currently uses Kotlin flow, but in the code there is an alternative RxJava 2 version of the classes. To run this version change the manifest to launch the Rx version of the main activity and rebuild. 


## License

    Copyright (C) 2020 David Wong

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
