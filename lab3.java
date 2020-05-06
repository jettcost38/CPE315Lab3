/*  Name: Isaiah Abrea
    Partner: Jett Costibolo
    Section: 03
    Description: This program prints the binary representation of an assembly file.
*/
import java.io.File;
import java.util.*;

public class lab2 {
    // holds each line of the program
    private static ArrayList<String> lines = new ArrayList<>();
    // each label is stored in here at the index of the # instruction it comes after
    private static String[] labels;
    // number of instructions. Used for putting labels into labels[]
    private static int num_inst = 0;

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

    // Reads the file and puts each line into the ArrayList lines
    public static void readFile(String file) throws Exception
    {
        String line;
        int num_lines = 0;
        File in = new File(file);
        Scanner scan = new Scanner(in);

        line = scan.nextLine();
        while(line != null)
        {
            // these are to make parsing comments and labels easier
            line = line.replaceAll("#"," #");
            line = line.replaceAll(":",": ");
            lines.add(line);
            num_lines++;
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

    // First pass.
    // Looks for a label by finding where colons are, and then puts the label into labels[] at the index of the # instruction it comes after
    public static void findLabels()
    {
        //initialize the label array
        labels = new String[lines.size()];
        // Goes through each line in the file and breaks it into tokens
        // if the line contains a label, it stores it.
        // if the line contains an instruction, it counts it and goes to the next.
        for(int i = 0; i < lines.size(); i++) {
            // Contains each part of the line as an array
            String[] tokens = lines.get(i).split(" |\t|\\$|,");
            for (int j = 0; j < tokens.length; j++) {
                if (tokens[j].contains("#")) {
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
                }
            }
        }
    }

    // Second pass. Prints each inst by calling its appropriate helper function
    public static void printInstruction()
    {
        int inst_num = 0;
        // Same setup as with finding labels, except it only finds instructions,
        // and once it does it prints them
        for(int i = 0; i < lines.size(); i++)
        {
            // Contains each part of the line as an array
            String[] tokens = lines.get(i).split(" |\t|\\$|,|:|\\(|\\)");
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
                    System.out.print("000000 ");
                    // This takes only the arguments of the instruction
                    r(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                    System.out.println(" 00000 100100");
                }
                else if(tokens[j].equals("or"))
                {
                    inst_num++;
                    System.out.print("000000 ");
                    r(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                    System.out.println(" 00000 100101");
                }
                else if(tokens[j].equals("add"))
                {
                    inst_num++;
                    System.out.print("000000 ");
                    r(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                    System.out.println(" 00000 100000");
                }
                else if(tokens[j].equals("addi"))
                {
                    inst_num++;
                    System.out.print("001000 ");
                    addi(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                }
                else if(tokens[j].equals("sll"))
                {
                    inst_num++;
                    System.out.print("000000 00000 ");
                    sll(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                    System.out.println(" 000000");
                }
                else if(tokens[j].equals("sub"))
                {
                    inst_num++;
                    System.out.print("000000 ");
                    r(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                    System.out.println(" 00000 100010");
                }
                else if(tokens[j].equals("slt"))
                {
                    inst_num++;
                    System.out.print("000000 ");
                    r(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                    System.out.println(" 00000 101010");
                }
                else if(tokens[j].equals("beq"))
                {
                    inst_num++;
                    System.out.print("000100 ");
                    b(inst_num, Arrays.copyOfRange(tokens, j + 1, tokens.length));
                }
                else if(tokens[j].equals("bne"))
                {
                    inst_num++;
                    System.out.print("000101 ");
                    b(inst_num, Arrays.copyOfRange(tokens, j + 1, tokens.length));
                }
                else if(tokens[j].equals("lw"))
                {
                    inst_num++;
                    System.out.print("100011 ");
                    loadStore(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                }
                else if(tokens[j].equals("sw"))
                {
                    inst_num++;
                    System.out.print("101011 ");
                    loadStore(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                }
                else if(tokens[j].equals("j"))
                {
                    inst_num++;
                    System.out.print("000010 ");
                    j(tokens[j + 1]);
                }
                else if(tokens[j].equals("jr"))
                {
                    inst_num++;
                    System.out.print("000000 ");
                    jr(Arrays.copyOfRange(tokens, j + 1, tokens.length));
                    System.out.println(" 000000000000000 001000");
                }
                else if(tokens[j].equals("jal"))
                {
                    inst_num++;
                    System.out.print("000011 ");
                    j(tokens[j + 1]);
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
            }
        }
    }

    // R-format. Used for and, or, add, sub, slt
    public static void r(String[] tokens)
    {
        // The array contains some blank elements, so this removes them
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        String rs = "";
        String rt = "";
        String rd = "";

        // Search for the registers' codes in the register list
        for(Register r: codes)
        {
            if(r.name.equals(t.get(1)))
            {
                rs = r.code;
            }
            if(r.name.equals(t.get(2)))
            {
                rt = r.code;
            }
            if(r.name.equals(t.get(0)))
            {
                rd = r.code;
            }
        }

        System.out.print(rs + " " + rt + " " + rd);
    }

    // Used for sll
    public static void sll(String[] tokens)
    {
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        String rt = "";
        String rd = "";

        for(Register r: codes)
        {
            if(r.name.equals(t.get(1)))
            {
                rt = r.code;
            }
            if(r.name.equals(t.get(0)))
            {
                rd = r.code;
            }
        }

        System.out.print(rt + " " + rd + " " + String.format("%05d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(t.get(2))))));
    }

    // Used for addi
    public static void addi(String[] tokens) {
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        String rt = "";
        String rs = "";

        for (Register r : codes) {
            if (r.name.equals(t.get(0))) {
                rt = r.code;
            }
            if (r.name.equals(t.get(1))) {
                rs = r.code;
            }
        }
        // Had to do an if/else here to ensure the immediate is printed out as a 16 bit number
        if (Integer.parseInt(t.get(2)) < 0)
        {
            short a = (short) Integer.parseInt(t.get(2));
            System.out.println(rs + " " + rt + " " + Integer.toBinaryString(0xFFFF & a));
        }
        else
        {
            System.out.println(rs + " " + rt + " " + String.format("%016d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(t.get(2))))));
        }
    }

    // Branching instructions. Used for beq, bne
    public static void b(int inst, String[] tokens) {
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        String rt = "";
        String rs = "";

        for (Register r : codes) {
            if (r.name.equals(t.get(1))) {
                rt = r.code;
            }
            if (r.name.equals(t.get(0))) {
                rs = r.code;
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
            System.out.println(rs + " " + rt + " " + Integer.toBinaryString(0xFFFF & a));
        }
        else
        {
            System.out.println(rs + " " + rt + " " + String.format("%016d", Integer.parseInt(Integer.toBinaryString((label_pos - inst)))));
        }
    }

    // J-format. Used for j, jal
    public static void j(String target)
    {
        int label_pos = 0;
        //search for the label
        for(int i = 0; i < labels.length; i++)
        {
            if(labels[i] != null && labels[i].equals(target))
            {
                label_pos = i;
            }
        }

        short a = (short) label_pos;
        System.out.println(String.format("%026d", Integer.parseInt(Integer.toBinaryString(a))));
    }

    // Used for jr
    public static void jr(String[] tokens)
    {
        List<String> t = new ArrayList<String>(Arrays.asList(tokens));
        t.removeAll(Arrays.asList("", null));
        for(Register r: codes)
        {
            if(r.name.equals(t.get(0)))
            {
                System.out.print(r.code);
            }
        }
    }

    // Load/Store instructions. Used for lw, sw
    public static void loadStore(String[] tokens)
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
        //readFile(args[0]);
        readFile("test1.asm");
        printMemory();
        //findLabels();
        //printInstruction();
    }
}