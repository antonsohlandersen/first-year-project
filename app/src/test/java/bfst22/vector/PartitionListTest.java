package bfst22.vector;

import bfst22.vector.Collections.PartitionList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PartitionListTest {

    PartitionList<String> partitionList = new PartitionList<>();

    @BeforeEach
    void setUp(){
        partitionList = new PartitionList<>();
        partitionList.add("Anders");
        partitionList.add("Mads");
        partitionList.add("Martin");
        partitionList.add("Anton");
        partitionList.add("Lukas");
    }

    @Test
    void testPartition(){
        var partitions = partitionList.partition(1);
        assertEquals(5, partitions.size());
    }

    @Test
    void testPartition2(){
        var partitions = partitionList.partition(5);
        assertEquals(1, partitions.size());
    }

    @Test
    void testPartition3(){
        var partitions = partitionList.partition(0);
        assertEquals(true, partitions.isEmpty());
    }

    @Test
    void testPartition4(){
        partitionList.clear();
        var partitions = partitionList.partition(5);
        assertEquals(true, partitions.isEmpty());
    }

}


