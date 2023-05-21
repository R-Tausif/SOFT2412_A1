# A. How to run the program:

1. Open the terminal.
2. Open the CC_02_Wed_12_Qifan_Group-5_A1 folder in the terminal.
3. Run "gradle run --console=plain", the additional flag is to remove the progress bar of gradle.
4. This will build the program automatically before running it.
5. Type the input according the prompt the program gives.
6. Enter after typing the input.
7. Repeat step 5-6.

## Commands
### SUMMARY
If **SUMMARY** is chosen the following will be outputted. The example user input format will be included in the square brackets. The system messages will be in *italic*.
<br/>

*Please enter the two currencies you want the summaries of (from, to):*
<br>
\[AUD]
<br>
\[SGD]

*Please enter the start and end dates (start, end):*
<br>
\[2022-09-01]
<br>
\[2022-09-13]

*The summary of AUD to SGD between 2022-09-01 and 2022-09-13 is:*
<br>
*Some summary of history rates:*
<br>
*The maximum is:*
<br>
*The median is:*
<br>
*The average is:*
<br>
*The standard deviation is:*
<br>
*The minimum is:*
<br/>

*Would you like to perform more operations? (YES/NO):*
<br>
[YES]

### CONVERT
If **CONVERT** is chosen the following will be outputted. The example user input format will be included in the square brackets. The system messages will be in *italic*.
<br/>

*You choose the CONVERT functionality*
<br>
*Please enter the money amount:* \[1000.0]
<br>
*Please enter its currency symbol:* \[AUD]
<br>
*Please enter the desired currency symbol you want to convert:* \[SGD]
<br/>

*The money amount after conversion is:*

## DISPLAY
If **DISPLAY** is chosen the following will be outputted. An example of system output will be in *italic*.
<br/>


&nbsp;&nbsp;F/T&nbsp;&nbsp;|&nbsp;&nbsp;SGD&nbsp;&nbsp;|&nbsp;&nbsp;CNY&nbsp;&nbsp;|&nbsp;&nbsp;GBP&nbsp;&nbsp;|&nbsp;&nbsp;AUD
<br>
\_______________________________________
<br>
&nbsp;&nbsp;SGD&nbsp;&nbsp;|1.00&nbsp;&nbsp;&nbsp;|4.92&nbsp;&nbsp;&nbsp;|0.62&nbsp;&nbsp;&nbsp;|1.15
<br>
\_______________________________________
<br>
&nbsp;&nbsp;CNY&nbsp;&nbsp;|0.20&nbsp;&nbsp;&nbsp;|1.00&nbsp;&nbsp;&nbsp;|0.13&nbsp;&nbsp;&nbsp;|0.21
<br>
\_______________________________________
<br>
&nbsp;&nbsp;GBP&nbsp;&nbsp;|1.62&nbsp;&nbsp;&nbsp;|7.97&nbsp;&nbsp;&nbsp;|1.00&nbsp;&nbsp;&nbsp;|3.25(I)
<br>
\_______________________________________
<br>
&nbsp;&nbsp;AUD&nbsp;&nbsp;|0.87&nbsp;&nbsp;&nbsp;|4.69&nbsp;&nbsp;&nbsp;|0.31(D)|1.00   

## UPDATE
If **UPDATE** is chosen the following will be outputted. The example user input format will be included in the square brackets. The system messages will be in *italic*.
<br/>

*You choose the UPDATE functionality*
*Please update currencies for 2022-09-12*
*Do you want to update existing currency rates? (YES/NO):* \[YES]
<br>
*Input currency to update from:* \[AUD]
<br>
*Input currency to update to:* \[SGD]
<br>
*Input new currency rate from AUD, to SGD:* \[1.025]
<br/>

*Do you want to add a new currency to the system? (YES/NO):* \[YES]
<br>
*Input new currency symbol:* \[EUR]
<br>
*Input the exchange rate from EUR to AUD:* \[1.5]
<br>
*Input the exchange rate from EUR to SGD:* \[2.0]
<br>
*Input the exchange rate from EUR to USD:* \[3.0]
<br>
*Input the exchange rate from EUR to GBO:* \[4.0]
<br>
*Input the exchange rate from EUR to JPY:* \[5.0]
<br>
*Input the exchange rate from EUR to CNY:* \[6.0]
<br\>

*Current most popular currencies are: \[SGD, CNY, GBP, AUD]*
<br>
*Do you want to update the most popular currencies? (YES/NO):* \[YES]
<br>
*Please input the first popular currency:* \[SGD]
<br>
*Please input the second popular currency:* \[GBP]
<br>
*Please input the third popular currency:* \[CNY]
<br>
*Please input the fourth popular currency:* \[AUD]
<br\>

*The new most popular currencies are [SGD, GBP, CNY, AUD]*


## Example:
User input is in square brackets  
<br/>
Please select the identity you want to login from below:  
ADMIN, USER  
Enter: \[USER]  
<br/>
Welcome USER!  
<br/>
Please select an option from below:  
EXIT, CONVERT, DISPLAY, SUMMARY  
Enter: \[EXIT]  
<br/>
Thank you for using the system~  
See you next time, bye!  

# B. How to test the program:

## Running the test cases.

1. Open terminal.
2. Open the CC_02_Wed_12_Qifan_Group-5_A1 folder in the terminal.
3. Run "gradle test"
4. This will run all the tests in the app\src\test\java\a1\
5. To check the result of the tests open app\build\reports\tests\test\index.html
6. This will display test results in a webpage.

## If you want to add a new test case

1. Create a new test class.
2. Write the test case in JUnit 5 format.
3. Run the steps from step 3 above.

# C. How to contribute to the project:

## Step 1: cloning the repository.

1. Get the repository link, either by going to the repo then get the clone link from there or copy this link:
https://github.sydney.edu.au/SOT2412-COMP9412-2022S2/CC_02_Wed_12_Qifan_Group-5_A1
2. Open terminal.
3. Go to the desired folder where you want to put the repository folder in.
4. Then run "git clone https://github.sydney.edu.au/SOT2412-COMP9412-2022S2/CC_02_Wed_12_Qifan_Group-5_A1"
5. Wait until the cloning process complete.

## Step 2: Collaborating.

### a. Making changes to one of the functionalities.

1. Checkout to the branch associated with the functionality.
2. Make some changes.
3. Once you want to merge your changes in the branch, make a pull request to merge your branch to the master branch.
4. Review the changes with the other team member.
5. Merge the branch.

### b. Making changes to the App.java.

1. Checkout to the master branch.
2. Make some changes.
3. Before you push ur changes to the online repo, review ur changes with the other teammates.
4. Push the changes to the master branch.

