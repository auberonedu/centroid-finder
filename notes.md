# ImageSummaryApp

1. The main method takes in a String[] of args to use in the application.
2. An if statement checks whether the length of args is less than 3. If so, it prints out an example of how many args should be passed into the program. This stops the rest of the program from running if less than 3 arguments are passed into the main method.
3. Three variables are declared. The first stores the input images path as a String from args[0]. The second stores the target hex color as a String from args[1]. The third stores the threshold as an integer from args[2]. The threshold uses a try-catch block to ensure that args[2] is an integer before reassigning it from 0. If it's not not an error message is printed out to the console and the method returns.
