/*  Name: Isaiah Abrea
    Partner: Jett Costibolo
    Section: 03
    Description: This program prints the binary representation of an assembly file.
*/
import java.io.File;
import java.util.*;

public class lab3 {
    // holds each line of the program
    private static ArrayList<String> lines = new ArrayList<>();
    // each label is stored in here at the index of the # instruction it comes after
    private static String[] labels;
    // number of instructions. Used for putting labels into labels[]
    private static int num_inst = 0;
    // used to keep track of which instruction we're on
    private static int inst_num = 0;

    private static Stack<Integer> memory = new Stack<>();

    // A class solely to make finding the register code easier
    public static class Register
    {
        public String name;
        public String code;
        public int val;

        public Register(String n, String c)
        {
            name = n;
            code = c;
            val = 0;
        }
    }

    // Holds all codes matched to registers for easy lookup.
    private static ArrayList<Register> codes = new ArrayList<Register>() {
        {
            add(new Register("pc", null));
            add(new Register("zero", "00000"));
            add(new Register("0", "00000"));
            add(new Register("v0", "00010"));
            add(new Register("v1", "00011"));
            add(new Register("a0", "00100"));
            add(new Register("a1", "00101"));
            add(new Register("a2", "00110"));
            add(new Register("a3", "00111"));
            add(new Register("t0", "01000"));
            add(new Register("t1", "01001"));
            add(new Register("t2", "01010"));
            add(new Register("t3", "01011"));
            add(new Register("t4", "01100"));
            add(new Register("t5", "01101"));
            add(new Register("t6", "01110"));
            add(new Register("t7", "01111"));
            add(new Register("s0", "10000"));
            add(new Register("s1", "10001"));
            add(new Register("s2", "10010"));
            add(new Register("s3", "10011"));
            add(new Register("s4", "10100"));
            add(new Register("s5", "10101"));
            add(new Register("s6", "10110"));
            add(new Register("s7", "10111"));
            add(new Register("t8", "11000"));
            add(new Register("t9", "11001"));
            add(new Register("sp", "11101"));
            add(new Register("ra", "11111"));
        }
    };

    // TODO: Format this correctly
    private static void printMemory()
    {
        for(Register r: codes)
        {
            if(!r.name.equals("zero"))
                System.out.println("$" + r.name + " = " + r.val);
        }
    }

    private static void clearMemory()
    {
        for(Register r: codes)
        {
            if(!r.name.equals("zero"))
                r.val = 0;
        }
        memory.clear();
    }

    // Reads the file and puts each line into the ArrayList lines
    public static void readFile(String file) throws Exception
    {
        String line;
        File in = new File(file);
        Scanner scan = new Scanner(in);

        line = scan.nextLine();
        while(line != null)
        {
            // these are to make parsing comments and labels easier
            line = line.replaceAll("#"," #");
            line = line.replaceAll(":",": ");
            lines.add(line);
            try
            {
                line = scan.nextLine();
            }
            catch (NoSuchElementException e)
            {
                break;
            }
        }
    }

    public static ArrayList<String> linesToRemove = new ArrayList<>();

    // First pass.
    // Looks for a label by finding where colons are, and then puts the label into labels[] at the index of the # instruction it comes after
    public static void findLabels()
    {
        //initialize the label array
        labels = new String[lines.size()];
        // Goes through each line in the file and breaks it into tokens
        // if the line contains a label, it stores it.
        // if the line contains an instruction, it counts it and goes to the next.
        for(String l : lines) {
            // Contains each part of the line as an array
            String[] tokens = l.split(" |\t|\\$|,");
            for (int j = 0; j < tokens.length; j++) {
                if (tokens[j].contains("#")) {
                    linesToRemove.add(l);
                    break;
                } else if (tokens[j].equals("and")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("or")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("add")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("addi")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("sll")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("sub")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("slt")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("beq")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("bne")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("lw")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("sw")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("j")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("jr")) {
                    num_inst++;
                    break;
                } else if (tokens[j].equals("jal")) {
                    num_inst++;
                    break;
                }
                // Here is where it puts the label into the array
                else if(tokens[j].contains(":")) {
                    labels[num_inst] = tokens[j].substring(0, tokens[j].length() - 1);
                    if(j == tokens.length - 1)
                    {
                        linesToRemove.add(l);
                    }
                }
                else if (j == tokens.length - 1)
                {
                    linesToRemove.add(l);
                }
            }
        }
        for(String l: linesToRemove)
        {
            lines.remove(l);
        }
    }

    // Runs the next instruction
    public static void parseInstruction()
    {
        // Contains each part of the line as an array
        String[] tokens = lines.get(inst_num).split(" |\t|\\$|,|:|\\(|\\)");
        for(int j = 0; j < tokens.length; j++)
        {
            // We don't need to see anything in comments
            if(tokens[j].contains("#"))
            {
                break;
            }
            else if(tokens[j].equals("and"))
            {
                inst_num++;
                //System.out.print("000000 ");
                // This takes only the arguments of the instruction
                r(Arrays.copyOfRange(tokens, j + 1, tokens.length), "and");
                //System.out.println(" 00000 100100");
            }
            else if(tokens[j].equals("or"))
            {
                inst_num++;
                //System.out.print("000000 ");
                r(Arrays.copyOfRange(tokens, j + 1, tokens.length), "or");
                //System.out.println(" 00000 100101");
            }
            else if(tokens[j].equals("add"))
            {
                inst_num++;
                //System.out.print("000000 ");
                r(Arrays.copyOfRange(tokens, j + 1, tokens.length), "add");
                //System.out.println(" 00000 100000");
            }
            else if(tokens[j].equals("addi"))
            {
                inst_num++;
                //System.out.print("001000 ");
                addi(Arrays.copyOfRange(tokens, j + 1, tokens.length));
            }
            else if(tokens[j].equals("sll"))
            {
                inst_num++;
                //System.out.print("000000 00000 ");
                sll(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                //System.out.println(" 000000");
            }
            else if(tokens[j].equals("sub"))
            {
                inst_num++;
                //System.out.print("000000 ");
                r(Arrays.copyOfRange(tokens, j + 1, tokens.length), "sub");
                //System.out.println(" 00000 100010");
            }
            else if(tokens[j].equals("slt"))
            {
                inst_num++;
                //System.out.print("000000 ");
                r(Arrays.copyOfRange(tokens, j + 1, tokens.length), "slt");
                //System.out.println(" 00000 101010");
            }
            else if(tokens[j].equals("beq"))
            {
                inst_num++;
                //System.out.print("000100 ");
                b(inst_num, Arrays.copyOfRange(tokens, j + 1, tokens.length), "beq");
            }
            else if(tokens[j].equals("bne"))
            {
                inst_num++;
                //System.out.print("000101 ");
                b(inst_num, Arrays.copyOfRange(tokens, j + 1, tokens.length), "bne");
            }
            else if(tokens[j].equals("lw"))
            {
                inst_num++;
                //System.out.print("100011 ");
                loadStore(Arrays.copyOfRange(tokens, j + 1, tokens.length), "lw");
            }
            else if(tokens[j].equals("sw"))
            {
                inst_num++;
                //System.out.print("101011 ");
                loadStore(Arrays.copyOfRange(tokens, j + 1, tokens.length), "sw");
            }
            else if(tokens[j].equals("j"))
            {
                inst_num++;
                //System.out.print("000010 ");
                j(tokens[j + 1], "j");
            }
            else if(tokens[j].equals("jr"))
            {
                inst_num++;
                //System.out.print("000000 ");
                jr(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                //System.out.println(" 000000000000000 001000");
            }
            else if(tokens[j].equals("jal"))
            {
                inst_num++;
                //System.out.print("000011 ");
                j(tokens[j + 1], "jal");
            }
            // If it is not an instruction, it could possibly be another recognized thing or an invalid instruction.
            // Error checking.
            else
            {
                boolean valid = false;
                // It's just whitespace
                if(tokens[j].equals(""))
                {
                    valid = true;
                }
                // It's a label
                for(String s: labels)
                {
                    if(s!= null && s.equals(tokens[j]))
                    {
                        valid = true;
                    }
                }
                // It's a register
                for(Register r: codes)
                {
                    if(r.name.equals(tokens[j]))
                    {
                        valid = true;
                    }
                }
                // It's an immediate
                for (char c : tokens[j].toCharArray()) {
                    if (Character.isDigit(c)) {
                        valid = true;
                    }
                }
                // If it is not one of those, it must be an invalid instruction.
                if(!valid)
                {
                    System.out.println("invalid instruction: " + tokens[j]);
                    return;
                }
            }
            //modify the stack if needed
            if(codes.get(codes.size() - 2).val > memory.size())
            {
                while(memory.size() < codes.get(codes.size() - 2).val)
                {
                    memory.push(0);
                }
            }
        }
    }

    // R-format. Used for and, or, add, sub, slt
    public static void r(String[] tokens, String cmnd)
    {
        // The array contains some blank elements, so this removes them
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        int rs = 0;
        int rt = 0;
        int rd = 0;

        // Search for the registers' codes in the register list
        for(Register r: codes)
        {
            if(r.name.equals(t.get(1)))
            {
                rs = r.val;
            }
            if(r.name.equals(t.get(2)))
            {
                rt = r.val;
            }
        }
        //System.out.print(rs + " " + rt + " " + rd);
        if(cmnd.equals("and")){
            rd = (rs & rt);
        }

        else if(cmnd.equals("or")){
            rd = (rs | rt);
        }

        else if(cmnd.equals("add")){
            rd = (rs + rt);
        }

        else if(cmnd.equals("sub")){
            rd = (rs - rt);
        }

        else if(cmnd.equals("slt")){
            if(rs < rt){
                rd = 1;
            }
            else{
                rd = 0;
            }
        }

        for(Register r: codes)
        {
            if(r.name.equals(t.get(0)))
            {
                r.val = rd;
            }
        }
    }

    // Used for sll
    public static void sll(String[] tokens)
    {
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        int rt = 0;
        int rd = 0;
        int numShift = Integer.parseInt(t.get(2));

        for(Register r: codes)
        {
            if(r.name.equals(t.get(1)))
            {
                rt = r.val;
            }
        }

        //System.out.print(rt + " " + rd + " " + String.format("%05d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(t.get(2))))));
        rd = rt << numShift;   // left shift operation numShift times

        for(Register r: codes)
        {
            if(r.name.equals(t.get(0)))
            {
                r.val = rd;
            }
        }
    }

    // Used for addi
    public static void addi(String[] tokens) {
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        int rt = 0;
        int rs = 0;
        int immediate = Integer.parseInt(t.get(2));

        for (Register r : codes) {
            if (r.name.equals(t.get(1))) {
                rs = r.val;
            }
        }
        // Had to do an if/else here to ensure the immediate is printed out as a 16 bit number
        /*if (Integer.parseInt(t.get(2)) < 0)
        {
            short a = (short) Integer.parseInt(t.get(2));
            //System.out.println(rs + " " + rt + " " + Integer.toBinaryString(0xFFFF & a));
        }
        else
        {
            //System.out.println(rs + " " + rt + " " + String.format("%016d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(t.get(2))))));
        }*/

        rt = rs + immediate;

        for (Register r : codes) {
            if (r.name.equals(t.get(0))) {
                r.val = rt;
            }
        }
    }

    // Branching instructions. Used for beq, bne
    public static void b(int inst, String[] tokens, String cmnd) {
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        int rt = 0;
        int rs = 0;

        for (Register r : codes) {
            if (r.name.equals(t.get(1))) {
                rt = r.val;
            }
            if (r.name.equals(t.get(0))) {
                rs = r.val;
            }
        }

        int label_pos = 0;
        //search for the label
        for(int i = 0; i < labels.length; i++)
        {
            if(labels[i] != null && labels[i].equals(t.get(2)))
            {
                label_pos = i;
            }
        }
        if ((label_pos - inst) < 0)
        {
            short a = (short) (label_pos - inst);
            //System.out.println(rs + " " + rt + " " + Integer.toBinaryString(0xFFFF & a));
        }
        else
        {
            //System.out.println(rs + " " + rt + " " + String.format("%016d", Integer.parseInt(Integer.toBinaryString((label_pos - inst)))));

        }

        if(cmnd == "beq"){
            if(rs == rt){
                for (Register r : codes) {
                    if (r.name.equals("pc")) {
                        r.val = r.val + 4 + label_pos;
                    }
                }
            }
        }

        else if(cmnd == "bne"){
            if(rs != rt){
                for (Register r : codes) {
                    if (r.name.equals("pc")) {
                        r.val = r.val + 4 + label_pos;     // pc = pc + 4 + branch address
                    }
                }
            }
        }
    }

    // J-format. Used for j, jal
    public static void j(String target, String cmnd)
    {
        int label_pos = 0;
        int pc_value;
        //search for the label
        for(int i = 0; i < labels.length; i++)
        {
            if(labels[i] != null && labels[i].equals(target))
            {
                label_pos = i;
            }
        }

        if(cmnd == "j"){
            for (Register r : codes) {
                if (r.name.equals("pc")) {
                    r.val = label_pos;       // pc = jump address
                }
            }
        }

        else if (cmnd == "jal"){
            for (Register r : codes) {
                if (r.name.equals("pc")) {
                    pc_value = r.val;
                    r.val = label_pos;       // pc = jump address
                }
            }
            codes[31] = pc_value + 4;  // idk what the ref sheet means by R[31] = PC + 4
        }
        //short a = (short) label_pos;
        //System.out.println(String.format("%026d", Integer.parseInt(Integer.toBinaryString(a))));
    }

    // Used for jr
    public static void jr(String[] tokens)
    {
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        /*for(Register r: codes)
        {
            if(r.name.equals(t.get(0)))
            {
                //System.out.print(r.code);
            }
        }*/

        int rs = 0;

        for (Register r : codes) {
            if (r.name.equals(t.get(0))) {
                rs = r.val;
                break;
            }
        }

        for (Register r : codes) {
            if (r.name.equals("pc")) {
                r.val = rs;
                break
            }
        }
    }

    // Load/Store instructions. Used for lw, sw
    public static void loadStore(String[] tokens, String cmnd)
    {
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        String rt = "";
        String rs = "";
        for(Register r: codes)
        {
            if(r.name.equals(t.get(0)))
            {
                rt = r.code;
            }
            if(r.name.equals(t.get(2)))
            {
                rs = r.code;
            }
        }
        if(Integer.parseInt(t.get(1)) < 0)
        {
            short a = (short) Integer.parseInt(t.get(1));
            System.out.println(rs + " " + rt + " " + Integer.toBinaryString(0xFFFF & a));
        }
        else
        {
            System.out.println(rs + " " + rt + " " + String.format("%016d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(t.get(1))))));
        }
    }

    public static void main(String[] args) throws Exception
    {
        Scanner scan = new Scanner(System.in);
        ArrayList<String> commands = new ArrayList<>();  // to store commands from script file

        //readFile(args[0]);
        readFile("test5.asm");
        //printMemory();
        findLabels();
        //parseInstruction();
        if(args.length > 1)
        {
            //script mode
            String command;
            File in = new File(args[1]);
            command = scan.nextLine();     // reading commands into its own array
            while(command != null)
            {
                commands.add(command);
                try
                {
                    command = scan.nextLine();
                }
                catch (NoSuchElementException e)
                {
                    break;
                }
            }

            for(int i = 0; i < commands.size(); i++){
                String[] input = commands.get(i).split(" ");  // command line -> [[command], [num1], [num2], ...)

                if(input[0].equals("q"))
                {
                    break;
                }

                else if(input[0].equals("d"))
                {
                    printMemory();
                }

                else if(input[0].equals("c")) {
                    clearMemory();
                    System.out.print("      Simulator reset\n");
                }

                else if(input[0].equals("s"))
                {
                    if(input[1] == null){
                        if(inst_num < lines.size())
                        {
                            parseInstruction();
                            System.out.print("      1 instruction(s) executed\n");
                        }
                    }
                    else{
                        int num = Integer.parseInt(input[1]);
                        for(int j = 0; j < num; j++){
                            parseInstruction();
                        }
                        System.out.print(String.format("      %d instruction(s) executed\n", num));
                    }
                }

                else if(input[0].equals("r"))
                {
                    // run through entire program
                }

                else if(input[0].equals("m"))
                {
                    int num1 = Integer.parseInt(input[1]);
                    int num2 = Integer.parseInt(input[2]);
                    for(int j = num1; j < (num2+1); j++){
                        System.out.println(String.format("[%d] = ", j) + codes.get(j).val);
                    }
                }

                else if(input[0].equals("h"))
                {
                    System.out.print("h = show help\n");
                    System.out.print("d = dump register state\n");
                    System.out.print("s = single step through the program (i.e. execute 1 instruction and stop)\n");
                    System.out.print("s num = step through num instructions of the program\n");
                    System.out.print("r = run until the program ends\n");
                    System.out.print("m num1 num2 = display data memory from location num1 to num2 \n");
                    System.out.print("c = clear all registers, memory, and the program counter to 0\n");
                    System.out.print("q = exit the program\n");
                }

                else
                {
                    continue;
                }
            }
        }

        else
        {
            //interactive mode
            while(true){

                System.out.print("mips> ");
                String user = scan.nextLine();
                String[] input = user.split(" ");

                if(input[0].equals("q"))
                {
                    break;
                }

                else if(input[0].equals("d"))
                {
                    printMemory();
                }

                else if(input[0].equals("c")) {
                    clearMemory();
                    System.out.print("      Simulator reset\n");
                }

                else if(input[0].equals("s"))
                {
                    if(input.length == 1){
                        if(inst_num < lines.size())
                        {
                            parseInstruction();
                            System.out.print("      1 instruction(s) executed\n");
                        }
                    }
                    else{
                        int num = Integer.parseInt(input[1]);
                        for(int i = 0; i < num; i++){
                            parseInstruction();
                        }
                        System.out.print(String.format("      %d instruction(s) executed\n", num));
                    }
                }

                else if(input[0].equals("r"))
                {
                    // run through entire program
                }

                else if(input[0].equals("m"))
                {
                    int num1 = Integer.parseInt(input[1]);
                    int num2 = Integer.parseInt(input[2]);
                    for(int j = num1; j < (num2+1); j++){

                        System.out.println(String.format("[%d] = ", j) + codes.get(j).val);

                    }
                }

                else if(input[0].equals("h"))
                {
                    System.out.print("h = show help\n");
                    System.out.print("d = dump register state\n");
                    System.out.print("s = single step through the program (i.e. execute 1 instruction and stop)\n");
                    System.out.print("s num = step through num instructions of the program\n");
                    System.out.print("r = run until the program ends\n");
                    System.out.print("m num1 num2 = display data memory from location num1 to num2 \n");
                    System.out.print("c = clear all registers, memory, and the program counter to 0\n");
                    System.out.print("q = exit the program\n");
                }

                else
                {
                    continue;
                }
            }
        }
    }
}