package googleInterview;

// see the .md file with the same name as this one (practise03_cardGame_maxPossDamage.md)

public class practise03_cardGame_maxPossDamage {
    public static void main(String[] args) {
        practise03_cardGame_maxPossDamage p = new practise03_cardGame_maxPossDamage();
        System.out.println(p.solution(5,3,new int[]{4,5,1}, new int[]{1,2,3}));;
    }

    public boolean solution(int total_money, int total_damage, int[] costs, int[] damages) {
        int[][] dp = new int[costs.length+1][total_money + 1]; //+1 in costs.length+1 for a buffer row
        for (int card = 0; card < costs.length+1; card++) {
            for (int tempMoneyCap = 0; tempMoneyCap < total_money + 1; tempMoneyCap++) {
                if (card == 0) {
                    dp[card][tempMoneyCap] = 0; // first row is buffer row. for when even the first item's cost < tempMoneyCap
                    continue;
                }
                if (tempMoneyCap == 0) {
                    dp[card][tempMoneyCap] = 0; // cannot afford anything so value zero
                    continue;
                }

                if (tempMoneyCap >= costs[card - 1]) {
                    int dontPick = dp[card - 1][tempMoneyCap];
                    int pick = dp[card - 1][tempMoneyCap - costs[card - 1]] + damages[card - 1];
                    dp[card][tempMoneyCap] = Math.max(
                            dontPick,
                            pick //don't miss -1 in dp[card-1]...
                            //... As this is a 0/1 problem, we can never take 2 of the same cost.
                            // So we only see the previous row.
                    );
                } else {
                    dp[card][tempMoneyCap] = dp[card - 1][tempMoneyCap];
                }

            }
        }
        int maxDamage = dp[costs.length][total_money];
        System.out.print("max:"+maxDamage+"  ");
        if (maxDamage > total_damage) {
            return true;
        }
        return false;
    }
}
