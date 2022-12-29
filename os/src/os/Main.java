package os;
import java.util.Scanner;
import java.util.Vector;
public class Main {

        public static void printMemoryInfo(Vector<Partition> partitions, Vector<Process> unallocatedProcesses2) {
            System.out.println("----------------------------------");
            System.out.println("Partitions:");
            for (Partition p : partitions) {
                System.out.print("- " + p.name + ": (" + p.size + " KB) => ");
                if (p.getProcess() != null)
                    System.out.println(p.getProcess().name);
                else
                    System.out.println("External Fragment");
            }
            if (unallocatedProcesses2.size() > 0) {
                System.out.print(">>> UnAllocated Processes ==> ");
                for (Process p : unallocatedProcesses2)
                    System.out.print(p.name + ", ");
            }
            System.out.println("----------------------------------");
    
        }
        private static final Scanner scanner = new Scanner(System.in);

        public static int readInt(String prompt, int min, int max) {
            int value;
            while (true) {
                System.out.print(prompt);
                value = scanner.nextInt();
                if (value >= min && value <= max)
                    break;
                System.out.println("Enter a value between " + min + " and " + max);
            }
            return value;
        }

        public static double readDouble(String prompt, double min, double max) {
            double value;
            while (true) {
                System.out.print(prompt);
                value = scanner.nextDouble();
                if (value >= min && value <= max)
                    break;
                System.out.println("Money Range(" + min + " and " + max + ") cm");
            }
            return value;
        }

        public static void main(String[] args) {
            // Take Memory.Memory Partitions From the User
            Vector<Partition> partitions = PartitionInput();
            // Take Processes From the User
            Vector<Process> processes = ProcessInput();

            // Select Allocation Method
            int choice;
            do {
                System.out.println("Memory Allocation Policy: ");
                System.out.println("[1] First Fit");
                System.out.println("[2] Best Fit");
                System.out.println("[3] Worst Fit");
                System.out.println("PRESS Any Other Key For Exit..");
                choice=scanner.nextInt();
           

                // Send deep to keep user input from change as we use this inputs many times
                Memory memory = new Memory(getDeepPartitions(partitions));
                switch (choice) {
                    case 1 -> {
                        memory.setAllocationPolicy(new FirstFit());
                    }
                    case 2 -> {
                        memory.setAllocationPolicy(new BestFit());
                    }
                    case 3 -> {
                        memory.setAllocationPolicy(new WorstFit());
                    }
                    default -> System.exit(0);
                }

                // Allocate Processes into Partitions
                Vector<Process> unallocatedProcesses = memory.allocate(getDeepProcesses(processes));
                System.out.println(unallocatedProcesses);

                // Print Partitions
                printMemoryInfo(memory.partitions, unallocatedProcesses);

                // Compact Memory?
                System.out.println("Compact Memory?");
                System.out.println("[1] YES");
                System.out.println("[2] NO");
                int choice2 = scanner.nextInt();
                if (choice2 == 1) {
                    memory.compact();
                    Vector<Process> unallocatedProcesses2 = memory.allocate(unallocatedProcesses);
                    printMemoryInfo(memory.partitions, unallocatedProcesses2);
                }
            } while (true);
        }
//

        public static Vector<Partition> PartitionInput() {
            int partitions_count = readInt("Number of Partitions:", 1, 10000);
            Vector<Partition> partitions = new Vector<>();
            for (int i = 0; i < partitions_count; i++) {
                System.out.println("Partition [" + (i + 1) + "] : ");
                System.out.print("Name: ");
                String name = new Scanner(System.in).nextLine();
                int size = readInt("Size: ", 1, 10000);
                partitions.add(new Partition(name, size));
            }
            return partitions;
        }

        public static Vector<Process> ProcessInput() {
            int processes_count =readInt("Number of Processes:", 1, 10000);
            Vector<Process> processes = new Vector<>();
            for (int i = 0; i < processes_count; i++) {
                System.out.println("- Process [" + (i + 1) + "] -");
                System.out.print("Name: ");
                String name = new Scanner(System.in).nextLine();
                int size = readInt("Size: ", 1, 10000);
                processes.add(new Process(name, size));
            }
            return processes;
        }

        public static Vector<Partition> getDeepPartitions(Vector<Partition> partitions) {
            Vector<Partition> deepPartitions = new Vector<Partition>();
            for (Partition p : partitions)
                deepPartitions.add(new Partition(p.name, p.size));
            return deepPartitions;
        }

        public static Vector<Process> getDeepProcesses(Vector<Process> processes) {
            Vector<Process> deepProcesses = new Vector<Process>();
            for (Process p : processes)
                deepProcesses.add(new Process(p.name, p.size));
            return deepProcesses;
        }


        
         
    }

