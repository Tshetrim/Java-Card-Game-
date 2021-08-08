import java.util.*;

public class Game {
    static int currentBalance = 500;
    static int totalWon = 0, totalLost = 0;
    static int roundCount = 1;
    static Deck deck = new Deck();
    static Scanner console = new Scanner(System.in);
    static boolean continuePlaying = true;
    static boolean playGame = true;

    public static void setStartingAmount(){
        try{
            String input = console.nextLine();
            int output = Integer.parseInt(input);
            if(output>0)
                currentBalance = output;
        }
        catch(NumberFormatException  e){
            System.out.print("Please reenter a valid starting amount: ");
            setStartingAmount();
        }
    }

    public static void startMenu(){
        System.out.println("--------------------Game Start-----------------------");
        System.out.println("Weclome to the betting game BBA");
        System.out.print("Please state how much money you are beginning with: ");
        setStartingAmount();
        System.out.println("Setting your bank as: "+currentBalance);
    }

    public static String getChoice(){
        while(true){
            String input = console.nextLine();
            if(input.equals("before") || input.equals("after") || input.equals("between")){
                return input;
            }
            else{
                System.out.print("Please reenter a valid choice of before, after, or in between: ");
            }
        }
    }

    public static int getBet(){
        while(true){
            String input = console.nextLine();
            try{
                int output = Integer.parseInt(input);
                if(output>0 && output<=currentBalance)
                    return output;
                else{
                    System.out.print("Please reenter a valid starting amount: ");
                }
            }
            catch(NumberFormatException  e){
                System.out.print("Please reenter a valid starting amount: ");
            }
        }
    }

    public static int calculatePosition(Card one, Card two, Card three){
        if(three.getValue()>two.getValue())
            return 2;
        else if(three.getValue()<one.getValue())
            return 0;
        else if(three.getValue()==two.getValue()){
            if(three.getSuit()>two.getSuit())
                return 2;
            else
                return 1; 
        }
        else if(three.getValue()==one.getValue()){
            if(three.getSuit()<one.getSuit())
                return 0;
            else
                return 1;
        }
        else
            return 1;
    }

    public static boolean calculateResult(int pos, String choice){
        int choiceInt = 0;
        if(choice.equals("after"))
            choiceInt = 2;
        if(choice.equals("between"))
            choiceInt = 1;
        
        if(choiceInt==pos)
            return true;
        else
            return false;
    }

    public static boolean checkIfContinue(){
        System.out.print("Press enter to continue playing, if you would like to stop, type stop: ");
        String input = console.nextLine();
        if(input.equals("stop"))
            return false;
        else
            return true;
    }

    public static void round(){
        //test();
        System.out.println("-----------------------Round " + roundCount + " Starting------------------------------");
        System.out.println("Balance is: "+currentBalance + "                     deck: "+deck.cardsLeft());

        Card card1 = deck.dealCard();
        Card card2 = deck.dealCard();
        System.out.println("The first card is: " + card1.getValueAsString() + " of "+ card1.getSuitAsString());
        System.out.println("The second card is: " + card2.getValueAsString() + " of "+ card2.getSuitAsString());

        System.out.println("Will the 3rd card be before the 1st card, between 1st and 2nd card, or after 2nd card");
        System.out.print("Please say before, between, after, or exit: ");
        String choice = getChoice();

        System.out.print("How much do you want to bet? ");
        int betAmount = getBet();

        Card card3 = deck.dealCard();
        System.out.println("The third card is: " + card3.getValueAsString() + " of "+ card3.getSuitAsString());
        int pos = calculatePosition(card1, card2, card3);

        boolean won = calculateResult(pos, choice);

        if(won){
            System.out.println("["+choice+"] "+"You won "+betAmount);
            currentBalance+=betAmount;
            totalWon+=betAmount;
        } else{
            System.out.println("["+choice+"] "+"You lost "+betAmount);
            currentBalance-=betAmount;
            totalLost+=betAmount;
        }

        roundCount++;   
    }

    public static void printResults(){
        System.out.println("--------------------Stats-----------------------");
        System.out.println("These are your results: ");
        System.out.println("You won a total of: $"+totalWon);
        System.out.println("You lost a total of: $"+totalLost);
        System.out.println("You played: "+roundCount+" times");
        System.out.println("There were "+deck.cardsLeft()+" cards left in the deck");
    }

    public static void reset(){
        totalWon = 0;
        totalLost = 0;
        roundCount = 1;
        deck = new Deck();
        deck.shuffle();
        continuePlaying = true; 
    }

    public static void handleEnd(int gameResult){
        System.out.println("--------------------Game Over-----------------------");
        if(gameResult==0){
            System.out.println("Sorry you are out of money, you lost");
        }
        else if(gameResult==1){
            System.out.println("Congratulations, you beat the house!");
        }
        else{
            System.out.println("Thank you for playing, come back next time!");
        }
        System.out.print("Write \"play\" to try again or enter to continue: ");
        String input = console.nextLine();
        if(input.equals("play")||input.equals("Play"))
            playGame = true;
        else
            playGame = false;
        printResults();
        reset();
    }


    public static void game(){
        while(playGame){
            startMenu();
            deck.shuffle();
            while(currentBalance>0 && deck.cardsLeft()>1 && continuePlaying){
                round();
                continuePlaying = checkIfContinue();
            }

            if(currentBalance<=0)
                handleEnd(0);
            else if(deck.cardsLeft()<=0)
                handleEnd(1);
            else
                handleEnd(3);
        }
 
    }

    public static void test(){
        for(int i =0; i<49; i++)
            deck.dealCard();
    }
    public static void main(String[] args) {
        game();
    }
}
