- Liban Notes 
4/14
Image Summary App
    -   main

            if(args.length < 3)
        This is going to be reading in the args - meaning the command in the console from the user, if it is less then the length 3 it will output something

            - the main has 3 main arguments its looking for
                - The input image location
                - the hex target color it is looking for
                - an integer for binarization - idk what that means 

        It is going to create 2 string variables 
        -   The first is going to be to store the image path
            inputImagePath
        - The second is going to be to store the target color
            hexTargetColor
        - It is going to create a third int variable for the threshold which again is going to be the 3rd argument from the command line via a try catch 

        It then goes on to make a BufferedImage variable
            -   From my understading, this is making the picture into a plane where each pixel or some portion of the picture is broken down into coordinates. 
        
        With the new BufferedImage var called inputImage
            It goes onto read the new image from the inputImagePath variable we defined which is sourced from the user input in the argument command. This is also done in a try catch 
    
        int target is created and generated our hexacode Number
            This is done by doing a parseInt passing in the hexTargetColor variable which is sourced from the user input and a arguemnt and as a second parameter the radix of 16.
            
            A radix is essentially defining what numbering system or base we want to convert it to. Since we want hexacode, we will use a radix of 16. If we wanted a binary base we would use a number like 2

        It creates a new ColorDistanceFinder variable called distanceFinder that is build on the EuclideanColorDistance

ColorDistanceFinder
    This is a interface that defines the distance between two different colors. They are taken in as parameters
EuclideanColorDistance
    This will be implementing ColorDistanceFinder and overides the inherited method

    This is going to be taking in TWO hexacodes and doing some logic to ID the difference between two different hexacode colors using bitwise operations?

    The main method continues to created a ImageBinarizer variable that is using the DistanceImageBinarizer class

    This is going to be making it to 0 or 1 into a 2d array? 

ImageBinarizer 
    This is a interface that will be responsible for translating a image from a image to and 2d array AND a 2D array to a black and white picture

    - It has two methods
        toBinaryArray
            This is going to take in a BufferedImage class variable and will be converting it into a binary 2d array. 0 rep black and 1 white. Returns a 2d Array
        toBufferedImage
            this takes the binary (0 or 1) array into a buffered image. (THE WHOLE THING WILL EITHER BE BLACK AND/OR WHITE)

            This means that for each 0 that is on the 2d Array it will produce either a white or Black pixel and produce an actual image. 
DistanceImageBinrizer
    This is going to implement the interface ImageBinrizer - The actual application of it.
    
    Before jumping in, it is important to understand
        That the threshhold variable from the args from the user is defining what number is allowed in terms of distance from the target hexacode/color. So if something is outside of that threshold it will be black, if not it will be white. 

        This is from the EuclideanColorDistance

    This includes a DistanceImageBinarizer method 
        Params - ColorFinder distanceFinder, int targetColor, int threshold

        These params are going to be used to define the distance between pixels, what our target color is and what our acceptable threshold is to define if a color is black or white

        This includes the same 
        toBinaryArray
        toBufferedImage
    

