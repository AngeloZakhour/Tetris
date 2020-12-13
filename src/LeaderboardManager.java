import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class LeaderboardManager {

    private static final String fileName = "Leaderboard.txt";

    public static ArrayList<String> addScore(String name, int score){
        ArrayList<String> leaderboard = readLeaderboard();

        if(leaderboard != null){
            int index = leaderboard.size();
            for(String s : leaderboard){
                if(score > Integer.parseInt(s.split(":")[1])){
                    index = leaderboard.indexOf(s);
                    break;
                }
            }
            leaderboard.add(index, name+":"+score);
        }
        else{
            leaderboard = new ArrayList<>(1);
            leaderboard.add(name+":"+score);
        }

        if(leaderboard.size() > 100) leaderboard = (ArrayList<String>) leaderboard.subList(0, 99);

        writeLeaderboard(leaderboard);


        return leaderboard;
    }

    public static ArrayList<String> readLeaderboard(){
        ArrayList<String> leaderboard = new ArrayList<>();
        File file = new File(fileName);
        if(file.exists()){
            try{
                Scanner sc = new Scanner(file);
                sc.nextLine();
                sc.nextLine();
                String line;
                while(sc.hasNextLine()){
                    line = sc.nextLine();
                    leaderboard.add(line.substring(line.indexOf(":")+1));
                }

                sc.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else return null;

        leaderboard.trimToSize();
        return leaderboard;
    }

    public static void writeLeaderboard(ArrayList<String> leaderboard){
        try{
            PrintWriter pw = new PrintWriter(new FileOutputStream(fileName));
            pw.println("Tetris Leaderboard:");
            pw.println("");

            for(int i=0; i<leaderboard.size(); i++){
                pw.println(i+1+":"+leaderboard.get(i));
            }

            pw.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
