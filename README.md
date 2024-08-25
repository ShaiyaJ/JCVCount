# JVCount
## About
This was another weekend project of mine where I tried to make a Java program while having no experience in programming in Java.

I did this, in part, because I wanted a challenge and also because I wanted to learn about computer vision and Java. I enjoyed my experience and created a "live writeup" of sorts that I want to put on my project showcase page at some point.

### Code
The code isn't super functional, it can just about handle English text. Although I started this project with the hopes that it would be able to detect Japanese vertical text. 

Both the Java and the computer vision code is all based on rough guesswork. I tried to minimise my use of google and just figure out problems. So there are probably fundemental computer vision errors and non-conventional Java code all over the place.

In the future, I will aim to improve my skills in these areas, which might spark some sort of motivation for me to come back to this project. However, for now, this project simply serves as a proof of concept on how quickly I can adapt to a new language and library.

## Use
To get the app to work, you must install the necessary libraries:
- OpenCV (opencv-490.jar)
- tess4j (net.sourceforge.tess4j on Maven)

There is also some files that you will need for the app to work, namely traineddata files. You can get traineddata files for many different lanugages here (https://github.com/tesseract-ocr/tessdata/tree/main). Although any traineddata files will work. These should be placed in a folder at the root of the project called `./tessdata`.

### Bugs
Occasionally multiple files don't get opened correctly.