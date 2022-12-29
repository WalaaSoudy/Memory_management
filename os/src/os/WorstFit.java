package os;

import java.util.Collections;
import java.util.Vector;

public class WorstFit implements Allocator{

	@Override
	public void sort(Vector<Partition> partitions) {
		// TODO Auto-generated method stub
		Collections.sort(partitions);
        Collections.reverse(partitions);
	}

}
