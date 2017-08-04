
# AutoImageSwitcher
A library for automatically switching images.

## Getting started

In your xml layout file
```java
...
<thuannv.autoimageswitcher.AutoImageSwitcher
    android:background="@android:color/black"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:delayAnimation="4000"
    app:inAnimation="@anim/image_switcher_in_animation"
    app:outAnimation="@anim/image_switcher_out_animation"
    app:images="@array/cover_images"
    />
...
```

## License

    Copyright (C) 2017 thuannv

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
