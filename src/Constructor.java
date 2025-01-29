import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;

public class Constructor {

    private static class Parameter {
        public String name;
        public String identifier;
        public boolean hasDefaults;
        public String def;

        public Parameter(String name, String identifier, boolean hasDefaults, String def){
            this.name = name;
            this.identifier = identifier;
            this.hasDefaults = hasDefaults;
            this.def = def;
        }

        @Override
        public String toString(){
            if(hasDefaults)
                return name + " " + identifier + " = " + def;
            else
                return name + " " + identifier;
        }
    }



    private static boolean isConstructorValid(String input) {
        // Regex to match a properly formatted constructor
        String regex = "^(\\w+(?:\\s+\\w+)*)?\\s*([A-Z]\\w*)\\s*\\((\\s*(\\w+\\s+\\w+(\\s*=\\s*[^,\\s]+)?)\\s*(,\\s*\\w+\\s+\\w+(\\s*=\\s*[^,\\s]+)?\\s*)*)?\\)\\s*.*$";
        // Compile and match the input
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(input).matches();
    }

    private static ArrayList<String> getParamsList(String input) {
        ArrayList<String> params = new ArrayList<>();
        String values = input.substring(input.indexOf("(") + 1, input.indexOf(")"));
        if (values.isEmpty()){
            System.out.println("no parameters");
            System.exit(1);
        }
        Collections.addAll(params, values.split(","));
        return params;
    }

    private static ArrayList<Parameter> getParams(ArrayList<String> paramsList) {
        ArrayList<Parameter> params = new ArrayList<>();

        for(String param : paramsList){
            param = param.trim();
            String[] tokens = param.split(" ");

            Parameter p;



            String name = tokens[0];
            String identifier = tokens[1];
            boolean hasDefaults = false;
            String def = null;

            if(tokens.length == 4){
                hasDefaults = true;
                def = tokens[3];

            }

            p = new Parameter(name, identifier, hasDefaults, def);
            params.add(p);

        }

        return params;
    }

    private void createCombinations(ArrayList<Parameter> params, int index, ArrayList<Parameter> currentCombination, String prefix) {
        // Base case: When we've processed all parameters, print the current combination
        if (index == params.size()) {
            boolean last = true;
            //System.out.println(currentCombination);
            StringBuilder method = new StringBuilder();
            method.append(prefix).append("(");

            for(int i = 0; i < currentCombination.size(); i++){
                Parameter p = currentCombination.get(i);
                if(!p.hasDefaults){
                    method.append(p.name).append(" ").append(p.identifier);
                    method.append(", ");
                }
                else{
                    last = false;
                }

            }
            method = new StringBuilder(method.substring(0, method.length() - 2));
            method.append("){");

            if(!last){
                method.append("\n\tthis(");
                for(int i = 0; i < currentCombination.size(); i++){
                    Parameter p = currentCombination.get(i);
                    if(p.hasDefaults){
                        method.append(p.def);
                    }
                    else{
                        method.append(p.identifier);
                    }
                    method.append(", ");
                }
                method = new StringBuilder(method.substring(0, method.length() - 2));
                method.append(");\n}");
            }
            else{
                method.append("\n\tsuper();");
                for(int i = 0; i < currentCombination.size(); i++){
                    Parameter p = currentCombination.get(i);
                    method.append("\n");
                    method.append("\tthis.").append(p.identifier).append(" = ").append(p.identifier).append(";");
                }
                method.append("\n}");
            }


            //System.out.println(method);
            combinations.add(String.valueOf(method));
            return;
        }

        // Get the current parameter
        Parameter param = params.get(index);

        // Case 1: Add the parameter with its default (if it has one)
        if (param.hasDefaults) {
            // Clone the parameter with the default value
            Parameter withDefault = new Parameter(param.name, param.identifier, param.hasDefaults, param.def);
            currentCombination.add(withDefault);
            createCombinations(params, index + 1, currentCombination, prefix);
            currentCombination.remove(currentCombination.size() - 1); // Backtrack
        }

        // Case 2: Add the parameter without using the default
        Parameter withoutDefault = new Parameter(param.name, param.identifier, false, null);
        currentCombination.add(withoutDefault);
        createCombinations(params, index + 1, currentCombination, prefix);
        currentCombination.remove(currentCombination.size() - 1); // Backtrack
    }

    private String beginning;
    private ArrayList<String> paramsList;
    private ArrayList<Parameter> params;
    private ArrayList<String> combinations;
    private boolean hasGenerated;

    public Constructor(){

        beginning = "";
        paramsList = new ArrayList<>();
        params = new ArrayList<>();
        combinations = new ArrayList<>();


    }

    public boolean generateCombinations(String input){
        if (!isConstructorValid(input)) {
            System.out.println(input + ": Invalid, constructor must be in form");
            System.out.println("MODIFIER* CLASS_NAME((PARAM_TYPE PARAM_NAME ?(= VALUE),*)");
            System.out.println("as a regex:");
            System.out.println("^(\\w+(?:\\s+\\w+)*)?\\s*([A-Z]\\w*)\\s*\\((\\s*(\\w+\\s+\\w+(\\s*=\\s*[^,\\s]+)?)\\s*(,\\s*\\w+\\s+\\w+(\\s*=\\s*[^,\\s]+)?\\s*)*)?\\)\\s*.*$");
            return false;
        }

        String beginning = input.substring(0,input.indexOf("("));
        ArrayList<String> paramsList = getParamsList(input);
        ArrayList<Parameter> params = getParams(paramsList);
        ArrayList<Parameter> currentCombination = new ArrayList<>();
        createCombinations(params, 0, currentCombination, beginning);

        return true;



    }

    public ArrayList<String> getCombinations(){
        return combinations;
    }





}
