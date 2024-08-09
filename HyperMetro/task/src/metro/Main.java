package metro;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Metro metro = new Metro();
        if(!metro.initializeFromJson(args[0])){
            return;
        };

        boolean exit = false;
        while(!exit) {
            String cmd = sc.nextLine();
            CommandParser p = new CommandParser(cmd);
            switch (p.command) {
                case "/append":
                    metro.addLast(p.args.get(0), p.args.get(1), "0");
                    break;
                case "/add-head":
                    metro.addFirst(p.args.get(0), p.args.get(1), "0");
                    break;
                case "/add":
                    metro.addLast(p.args.get(0), p.args.get(1), p.args.get(2));
                    break;
                case "/remove":
                    metro.delete(p.args.get(0), p.args.get(1));
                    break;
                case "/output":
                    metro.printLine(p.args.get(0));
                    break;
                case "/connect":
                    metro.connect(p.args.get(0), p.args.get(1), p.args.get(2), p.args.get(3));
                    break;
                case "/route":
                    metro.route(p.args.get(0), p.args.get(1), p.args.get(2), p.args.get(3));
                    break;
                case "/fastest-route":
                    metro.fastestRoute(p.args.get(0), p.args.get(1), p.args.get(2), p.args.get(3));
                    break;
                case "/exit":
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid command");
                    break;

            }
        }
/*

        metro.printLine("Hammersmith-and-City");

 */
    }
}
