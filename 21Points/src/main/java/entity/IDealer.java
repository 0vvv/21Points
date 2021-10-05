package entity;

public interface IDealer{
    // 给玩家发牌
    void dealCard(GameParticipate gameParticipate);

    // 洗牌
    void shuffleDeck();

    // 游戏结束时，清算玩家得分，返回1玩家胜利，返回2玩家失败，返回0平局
    int liquidateAssets(GameParticipate gameParticipate);
}
