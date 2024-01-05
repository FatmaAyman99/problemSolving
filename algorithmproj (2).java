import java.util.*;

public class algorithmProj {

    //SOLUTION 1 (EXHASTIVE METHOD)
    public static Set<Integer> exhaustiveMethod(int[][] processes, int T) {
        int n = processes.length;
        int max_value = 0;
        Set<Integer> max_set0 = new HashSet<>();
        for (int i = 0; i < (1 << n); i++) {          //2^n -1
            Set<Integer> subset = new HashSet<>();
            int value = 0;
            int duration = 0;
            for (int j = 0; j < n; j++) {  //generates all possible subsets of the processes array. 
                if ((i & (1 << j)) != 0) {
                    subset.add(j + 1);
                    duration += processes[j][1];
                    value += processes[j][2];
                }
            }
            if (duration <= T && value > max_value) { //if it exceeds the limit and if value greater than the max value
                max_value = value;
                max_set0 = subset;
            }
        }
        return max_set0;
    }

    //SOLUTION 2 (GREEDY)

    public static Set<Integer> greedyMethod(int[][] processes, int T) {
        int n = processes.length;
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> b[2] - a[2]); // max heap 
        for (int i = 0; i < n; i++) {
            pq.offer(processes[i]);
        }
        Set<Integer> subset = new HashSet<>();
        int duration = 0;
        int value = 0;
        while (!pq.isEmpty() && duration < T) { //picks the processes with the highest value and add it to the set
            int[] process = pq.poll();
            if (duration + process[1] <= T) {
                subset.add(process[0]);
                duration += process[1];
                value += process[2];
            }
        }
        for (int i : subset) {
            System.out.print(i + " "); //outputs the selected processes
        }
        System.out.println("");
        System.out.println("Total value: " + value);
        System.out.println("Duration: " + duration);
        return subset;
    }

    // SOLUTION 3 (DYNAMIC PROG)

    public static  int dpMethod(int[][] processes, int T) {
        int n = processes.length;
        int[][] memo = new int[n + 1][T + 1]; //defining the memory to store
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= T; j++) {
                int value = processes[i - 1][2];
                int duration = processes[i - 1][1];
                if (duration > j) {
                    memo[i][j] = memo[i - 1][j];
                } else {
                    memo[i][j] = Math.max(memo[i - 1][j], memo[i - 1][j - duration] + value);
                } //calculate the max value of each cell in the table if the duration of p exceeds t then skip
            }
        }
        int duration = 0;
        int i = n, j = T;
        while (i > 0 && j > 0) {
            if (memo[i][j] != memo[i - 1][j]) { // top down trace
                duration += processes[i - 1][1];
                System.out.print(processes[i-1][0] + " "); //print the selected process
                j -= processes[i - 1][1];
                
                i--;
                
            } else {
                i--;
            }
        }
        System.out.println(" ");
        System.out.println("Total value: " + memo[n][T]);
        System.out.println("Duration: " + duration);
        return memo[n][T];
    }



    public static void main(String[] args) {
        int[][] processes = {{1, 3, 21}, {2, 6, 24}, {3, 2, 12}, {4, 4, 20}};
        int T = 8;


        //SOLUTION 1:
        System.out.println("SOLUTION 1:");
        long startTime = System.nanoTime();
        Set<Integer> max_set = exhaustiveMethod(processes, T);
        int max_value = 0;
        int duration = 0;
        
        for (int i : max_set) {
            max_value += processes[i - 1][2];
            duration += processes[i - 1][1];
            System.out.print(i + " ");
        }
        long endTime = System.nanoTime();
        System.out.println();
        System.out.println("Total value: " + max_value);
        System.out.println("Duration: " + duration);
        System.out.println("Running time: " + (endTime - startTime));

        //SOLUTION 2:
        System.out.println("SOLUTION 2:");
        long startTime1 = System.nanoTime();
        Set<Integer> max_set2 = greedyMethod(processes, T);
        long endTime1 = System.nanoTime();
        System.out.println("Running time: " + (endTime1 - startTime1));

        //SOLUTION 3:
        System.out.println("SOLUTION 3:");
        long startTime2 = System.nanoTime();
        dpMethod(processes, T);
        long endTime2 = System.nanoTime();
        System.out.println("Running time: " + (endTime2 - startTime2));

    }
}