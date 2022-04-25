import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
public class CommonTest {
    @Test
    void testTwo() {
        var list = List.of(
                "  Эта модель	помогает нейросети запоминать правила языка",
                "выбирать подходящие слова и связывать их	 по смыслу",
                "   Также эта нейросеть	 помогает превращать введённый текст "
        );

        final Map<String, Long> collect = list.stream()
                .map(String::trim)
                .map(e -> {
                    final String[] split = e.split("\\s+");
                    return Arrays.asList(split);
                }).flatMap(Collection::stream)
                .map(String::toUpperCase)
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        log.info("{}", collect);
        Assertions.assertEquals(20, collect.size());
    }

    @Test
    void testOne() {
        Node node1 = new Node("1");
        final Node node2 = new Node("2");
        node1.setNode(node2);
        final Node node3 = new Node("3");
        node2.setNode(node3);
        final Node node4 = new Node("4");
        node3.setNode(node4);

        final Node node5 = invert(node1);
        Assertions.assertEquals("1",node5.getNode().getNode().getNode().getName());
        Assertions.assertEquals("4",node5.getName());
    }

    private Node invert(Node node) {
        // Вычисляем глубину вложенности (размер)
        final int nodeSize = getNodeSize(node);
        log.info("{}", nodeSize);
        Node result = null;
        Node prevN = null;

        //  Проходимся в обратном порядке
        for (int i = nodeSize; i >= 0; i--) {
            // Берем ноду по индексу
            Node currentN = getNodeByIndex(node, i);

            // Первая с конца - будущий результат.
            if (result == null && currentN != null) {
                result = currentN;
            }

            // Меняем порядок вложенности.
            if (prevN != null) {
                prevN.setNode(currentN);
            }
            prevN = currentN;

            // Последний элемент никуда не ссылается
            if (i == 0 && currentN != null) {
                currentN.setNode(null);
            }
        }
        return result;
    }

    private int getNodeSize(Node node) {
        int result = 0;
        Node tmp = node;
        while (tmp.getNode() != null) {
            tmp = tmp.getNode();
            result++;
        }
        return result;
    }

    private Node getNodeByIndex(Node node, int index) {

        if (index == 0) {
            final Node r = new Node(node.getName());
            r.setNode(node.getNode());
            return r;

        }

        int count = 1;
        Node tmp = node;

        while (tmp != null && tmp.getNode() != null) {
            tmp = tmp.getNode();
            if (tmp != null && index == count) {
                final Node result = new Node(tmp.getName());
                result.setNode(tmp.getNode());
                return result;
            }
            count++;
        }
        return null;
    }



    @Setter
    @Getter
    static class Node {
        public Node(String name) {
            this.name = name;
        }

        private String name;
        private Node node;
    }
}
