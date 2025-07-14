public class Question {
    private int id;
    private String questiontext;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String difficulty;
    private String topic;
    private String language;


    /**
     * Default constructor that creates an empty Question object.
     */
    // Constructors
    public Question() {}

    /**
     * Constructor that initializes a Question object with all fields: id, question text, options,
     * correct answer, topic, and language.
     */
    public Question(int id, String questiontext, String optionA, String optionB, String optionC, String optionD,
                    String correctAnswer,String difficulty,  String topic, String language) {
        this.id = id;
        this.questiontext = questiontext;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty ;
        this.topic = topic;
        this.language = language;
    }
    /**
     * Constructor that initializes a Question object with the question text, answer options, and the correct answer.
     */
    public Question(String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer) {
        this.questiontext = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
    }


    public Question( String questionText, String optionA, String optionB, String optionC, String optionD) {
        this.questiontext = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this. optionC = optionC;
        this.optionD = optionD;

    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questiontext ;
    }
    public void setText(String text){
        this.questiontext = questiontext;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty){this.difficulty = difficulty;}

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questiontext='" + questiontext + '\'' +
                ", optionA='" + optionA + '\'' +
                ", optionB='" + optionB + '\'' +
                ", optionC='" + optionC + '\'' +
                ", optionD='" + optionD + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", topic='" + topic + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
