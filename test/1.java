public class Main {
    public static void main(String[] args) {
 
        // 进行一千万次测试
        int times = 10000000;
        // 第一次命中计数
        int firstCount = 0;
        // 第二次命中计数
        int secondCount = 0;
        while (times-- > 0) {
            // 第一次选择的盒子地址（在三个盒子里选择）
            int firstSelectedIndex = -1;
            // 主持人选择的盒子地址（在未被选择的两个盒子中选择一个）
            int hostSelectedIndex = -1;
            // 第二次选择的盒子地址（在被主持人去掉后的两个盒子里选择）
            int secondSelectedIndex = -1;
            // 盒子数组，存放三个盒子，初始值默认为false，即无宝物
            boolean[] boxes = new boolean[3];
            // 取其中随机一个盒子设为true，即有宝物
            boxes[(int) (Math.random() * 3)] = true;
            // 从三个盒子中随机选择一个盒子，作为第一次选择
            firstSelectedIndex = (int) (Math.random() * 3);
            // 用于记录两个第一次未被选择的盒子的地址的数组
            int[] firstNotSelected = {-1, -1};
            // 记录两个第一次未被选择的盒子的地址
            for (int i = 0, j = 0; i < boxes.length; i++) {
                if (i != firstSelectedIndex) {
                    firstNotSelected[j] = i;
                    j++;
                }
            }
            // 从两个第一次未被选择的盒子里挑一个空盒子作为主持人选择的盒子
            for (int i = 0; i < firstNotSelected.length; i++) {
                if (!boxes[firstNotSelected[i]]) {
                    hostSelectedIndex = firstNotSelected[i];
                    break;
                }
            }
            // 用于记录两个主持人没选的空盒的地址的数组
            int[] hostNotSelectedIndex = {-1, -1};
            // 记录两个主持人没选的空盒的地址
            for (int i = 0, j = 0; i < boxes.length; i++) {
                if (i != hostSelectedIndex) {
                    hostNotSelectedIndex[j] = i;
                    j++;
                }
            }
            // 选择第二次
            secondSelectedIndex = hostNotSelectedIndex[(int) (Math.random() * 2)];
            // 第二次不能和第一次相同，即改变选择
            while (secondSelectedIndex == firstSelectedIndex) {
                secondSelectedIndex = hostNotSelectedIndex[(int) (Math.random() * 2)];
            }
            // 判断两次命中情况
            if (boxes[firstSelectedIndex])
                firstCount++;
            if (boxes[secondSelectedIndex])
                secondCount++;
        }
        // 显示命中情况
        System.out.println("共测试" + (firstCount + secondCount) + "次");
        System.out.println("第一次（未改变选择）猜中" + firstCount + "次");
        System.out.println("第二次（改变选择）  猜中" + secondCount + "次");
    }
}