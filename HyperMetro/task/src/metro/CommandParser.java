package metro;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser{
    public String command = "";
    List<String> args = new ArrayList<String>();

    public CommandParser(String cmd){
        extractCommand(cmd);
        cmd = removeString(cmd, this.command);
        while(!cmd.isEmpty()){
            String arg = extractParameters(cmd);
            cmd = removeString(cmd, arg).trim();
            if(!arg.isEmpty()) args.add(arg.replaceFirst("^\"", "").replaceAll("\"$", ""));
        }
    }

    private String removeString(String str, String sub){
        return str.replaceFirst(sub, "").trim();
    }

    private String extractParameters(String cmd){
        Pattern pattern = Pattern.compile("^[\\\"][A-Za-z\\-0-9&\\. ]*[\\\"]|[^ \\\"]*");
        Matcher matcher = pattern.matcher(cmd);
        String arg = "";

        if (matcher.find()) {
            arg = matcher.group().trim();
        }
        return arg;
    }

    private void extractCommand(String cmd){
        Pattern pattern = Pattern.compile("^[\\/][a-z\\-A-Z]*([ ]|$)");
        Matcher matcher = pattern.matcher(cmd);

        if (matcher.find()) {
            this.command = matcher.group().trim();
        }
    }
}
