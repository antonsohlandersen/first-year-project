package bfst22.vector.Collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//A PartitionList is an ArrayList that can be partitioned into smaller lists
//based on implementation from https://www.amitph.com/java-partition-list/
public class PartitionList<E> extends ArrayList<E> {
    public static final long serialVersionUID = 9082420;

    public PartitionList() {
        super();
    }

    public PartitionList(Collection<? extends E> c) {
        super(c);
    }

    //Partitions itself into a PartitionsList of two PartitionLists (Used for KdTree)
    public PartitionList<PartitionList<E>> partitionToPartionLists(int sizeOfEachPartition) {
        PartitionList<PartitionList<E>> partitions = new PartitionList<>();

        if (this.size() == 0) {
            return partitions;
        }

        if (sizeOfEachPartition == 0) {
            return partitions;
        }

        int length = this.size();

        int numOfPartitions = length / sizeOfEachPartition;

        boolean addOneElementToSecondPartition = false;
        if(length % sizeOfEachPartition != 0 || (length%2 != 0 && sizeOfEachPartition == 1)){
            addOneElementToSecondPartition = true;
        }

        for (int currentPartition = 0; currentPartition < numOfPartitions; currentPartition++) {
            int from = currentPartition * sizeOfEachPartition;
            int to = Math.min((currentPartition * sizeOfEachPartition + sizeOfEachPartition), length);
            if(addOneElementToSecondPartition && currentPartition == 1){ to += 1; }
            PartitionList<E> temp = new PartitionList<>();
            for (int j = from; j < to; j++) {
                temp.add(this.get(j));
            }
            partitions.add(temp);
        }

        return partitions;
    }

    //Partitions itself into an ArrayList of PartitionLists (Used for RTree)
    public List<List<E>> partition(int sizeOfEachPartition) {
        List<List<E>> partitions = new ArrayList<>();

        if (this.size() == 0) {
            return partitions;
        }
        if (sizeOfEachPartition == 0) {
            return partitions;
        }

        int length = this.size();

        int numOfPartitions = length / sizeOfEachPartition + ((length % sizeOfEachPartition == 0) ? 0 : 1);

        for (int currentPartition = 0; currentPartition < numOfPartitions; currentPartition++) {
            int from = currentPartition * sizeOfEachPartition;
            int to = Math.min((currentPartition * sizeOfEachPartition + sizeOfEachPartition), length);
            PartitionList<E> temp = new PartitionList<>();
            for (int j = from; j < to; j++) {
                temp.add(this.get(j));
            }
            partitions.add(temp);
        }

        return partitions;
    }
}
