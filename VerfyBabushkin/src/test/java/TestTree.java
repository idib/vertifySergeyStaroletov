import com.BTree.BTree;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestTree {
    @Test
    void treeTest(){
        BTree<Integer, Integer> tree = new BTree<>(4);
        tree.put(1, 2);
        assertNotNull(tree);
        assertEquals(true, tree.containsKey(1));
        assertEquals(false, tree.containsKey(2));
        assertEquals(1, tree.getSize());
        tree.del(1);
        assertEquals(false, tree.containsKey(1));
        assertEquals(0, tree.getSize());
    }

    @Test
    void sampleTest() {
        BTree<Integer, Integer> tree = new BTree<>(4);
        tree.put(1,0);
        tree.put(1,0);
        tree.put(1,0);
        tree.put(1,0);
        tree.put(1,0);
        assertEquals(true, tree.containsKey(1));
        assertEquals(1, tree.getSize());
        tree.del(0);
        assertEquals(1, tree.getSize());
    }

    @RepeatedTest(10)
    void treeTestWithSet() {
        Set<Integer> set = new HashSet<>();
        BTree<Integer, Integer> btree = new BTree<>(4);
        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            int cur = random.nextInt(10);
            if (random.nextBoolean()) {
                btree.put(cur, 0);
                set.add(cur);
            } else {
                btree.del(cur);
                set.remove(cur);
            }
            assertEquals(set.contains(cur), btree.containsKey(cur));
            assertEquals(set.size(), btree.getSize());
        }
    }
}
