package os;

import java.util.Vector;

public class Memory {
	Allocator allocationPolicy;
    public Vector<Partition> partitions;
    public Memory(Vector<Partition> partitions) {
        this.partitions = partitions;
        this.allocationPolicy = new FirstFit(); // Default
    }
    public void setAllocationPolicy(Allocator allocationPolicy) {
        this.allocationPolicy = allocationPolicy;
    }
    public Vector<Process> allocate(Vector<Process> jobProcesses) {
        allocationPolicy.sort(partitions);

        Vector<Process> unallocProcess = new Vector<>();
        for (Process process : jobProcesses) {
            boolean isAllocated = false;
            for (int j = 0; j < partitions.size(); j++) {
                Partition partition = partitions.get(j);
                if (partition.isEmpty() && partition.size >= process.size) {
                    int extraSpace = partition.size - process.size;
                    partition.setProcess(process);
                    if (extraSpace > 0)
                        partitions.insertElementAt(new Partition("Partition"+(partitions.size()), extraSpace), j + 1);
                    isAllocated = true;
                    break;
                }
            }
            if (!isAllocated)
                unallocProcess.add(process);
        }
        return unallocProcess;
    }
    public void compact() {
        int total = 0;
        for (Partition p : partitions) {
            if (p.getProcess() == null)
                total += p.size;
        }
        // Remove all empty spaces
        partitions.removeIf(i -> i.getProcess() == null);
        if (total > 0)
            // Add the new Compacted Space
            partitions.add(new Partition("Partition"+(partitions.size()+1), total));
    }
}
