package puzzle.slider.vn.Entity;

/**
 * 
 * @sine Mar 18, 2013
 */
public class GameHistory {

//	private int id;
	private String idGame;
	private String imageName;
	private String folderPath;
	private int type;
	private int levelStar;
	private int levelTime;
	private boolean isFinish;
	private String googleId;
	
	
	public GameHistory() {
//		this.id = id;
		this.idGame = "";
		this.imageName = "";
		this.folderPath = "";
		this.type = 0;
		this.levelStar = 0;
		this.levelTime = 0;
		this.isFinish = false;
		this.googleId = "";
	}
	
	/**
	 * 
	 * @since Mar 18, 2013
	 * @param id
	 * @param imageName
	 * @param folderPath
	 * @param type The type for slider or board: type = 1 is slider, type = 2 is board
	 * @param levelStar
	 * @param levelTime
	 */
	public GameHistory(String idGame,String imageName, String folderPath,int type,	int levelStar,int levelTime) {
//		this.id = id;
		this.idGame = idGame;
		this.imageName = imageName;
		this.folderPath = folderPath;
		this.type = type;
		this.levelStar = levelStar;
		this.levelTime = levelTime;
	}

	/**
	 * @return the id
	 */
//	public int getId() {
//		return id;
//	}

	/**
	 * @param id the id to set
	 */
//	public void setId(int id) {
//		this.id = id;
//	}

	/**
	 * @return the imageName
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * @param imageName the imageName to set
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * @return the folderPath
	 */
	public String getFolderPath() {
		return folderPath;
	}

	/**
	 * @param folderPath the folderPath to set
	 */
	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	/**
	 * type = 1 is slider
	 * type = 2 is board
	 * 
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * set Type is slider or board
	 * <br \>
	 * type = 1 is slider
	 * <br \>
	 * type = 2 is board
	 * 
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the levelStar
	 */
	public int getLevelStar() {
		return levelStar;
	}

	/**
	 * @param levelStar the levelStar to set
	 */
	public void setLevelStar(int levelStar) {
		this.levelStar = levelStar;
	}

	/**
	 * @return the levelTime
	 */
	public int getLevelTime() {
		return levelTime;
	}

	/**
	 * @param levelTime the levelTime to set
	 */
	public void setLevelTime(int levelTime) {
		this.levelTime = levelTime;
	}

	/**
	 * @return the idGame
	 */
	public String getIdGame() {
		return idGame;
	}

	/**
	 * @param idGame the idGame to set
	 */
	public void setIdGame(String idGame) {
		this.idGame = idGame;
	}

	/**
	 * @return the isFinish
	 */
	public boolean isFinish() {
		return isFinish;
	}

	/**
	 * @param isFinish the isFinish to set
	 */
	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}
	
	/**
	 * 
	 * @since Apr 2, 2013 - 10:18:08 AM
	 * @return
	 */
	public String getGoogleId() {
		return googleId;
	}
	/**
	 * 
	 * @since Apr 2, 2013 - 10:18:05 AM
	 * @param googleId
	 */
	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}
}
