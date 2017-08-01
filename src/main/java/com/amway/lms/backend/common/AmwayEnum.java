package com.amway.lms.backend.common;

public final class AmwayEnum {
	public static enum CourseStatus {
		WAITING_FOR_APPROVAL("Waiting for Approval", 1), ACCEPTED("Accepted", 2), DENIED("Denied", 3), IN_PROGRESS("In Progress", 4), COMPLETED("Completed", 5);

		private CourseStatus(String strValue, Integer value) {
			this.strValue = strValue;
			this.value = value;
		}

		private final String strValue;
		private final Integer value;

		public String getStrValue() {
			return strValue;
		}

		public Integer getValue() {
			return value;
		}

	}
	
	public static enum SessionStatus{
	    NOT_STARTED("Not Started", 1), IN_PROGRESS("In Progress", 2), COMPLETED("Completed", 3);
	    private final String strValue;
	    private final Integer value;
	    private SessionStatus(String strValue, Integer value){
	        this.strValue = strValue;
	        this.value = value;
	    }
        public String getStrValue() {
            return strValue;
        }
        public Integer getValue() {
            return value;
        }
	    
	}

	public static enum CompletionStatus {
		WAITING_FOR_APPROVAL("Waiting for Approval", 1),ACCEPTED("Accepted", 2), DENIED("Denied", 3), PARTICIPATED("Participated", 4), ENDED_SESSION("Ended Session", 5), COMPLETED("Completed", 6);

		private CompletionStatus(String strValue, Integer value) {
			this.strValue = strValue;
			this.value = value;
		}

		private final String strValue;
		private final Integer value;

		public String getStrValue() {
			return strValue;
		}

		public Integer getValue() {
			return value;
		}

	}
	


	public static enum ManagerActionStatus {
		WAITING_FOR_APPROVAL("Waiting for Approval", 1), ACCEPTED("Accepted", 2), DENIED("Denied", 3);
		private ManagerActionStatus(String strValue, Integer value) {
			this.strValue = strValue;
			this.value = value;
		}

		private final String strValue;
		private final Integer value;

		public String getStrValue() {
			return strValue;
		}

		public Integer getValue() {
			return value;
		}

	}
	
	public static enum TraineeActionStatus {
		WAITING_FOR_APPROVAL("Waiting for Approval", 1), ACCEPTED("Accepted", 2), DENIED("Denied", 3);
		private TraineeActionStatus(String strValue, Integer value) {
			this.strValue = strValue;
			this.value = value;
		}

		private final String strValue;
		private final Integer value;

		public String getStrValue() {
			return strValue;
		}

		public Integer getValue() {
			return value;
		}

	}
	
	public static enum TrainingActionResult {
	    NOT_EVALUATE("Not Evaluate", 0), PASSED("Passed", 1), FAILED("Failed", 2);
	    private TrainingActionResult(String strValue, Integer value) {
	     this.strValue = strValue;
	     this.value = value;
	    }

	    private final String strValue;
	    private final Integer value;

	    public String getStrValue() {
	     return strValue;
	    }

	    public Integer getValue() {
	     return value;
	    }

	   }

	public static enum ColorWithStatus {
		WAITING_FOR_APPROVAL("#A569BD"), 
		ACCEPTED("#2980B9"), 
		DENIED("#D9534F"), 
		INPROGRESS("#85C1E9"),
		COMPLETED("#5CB85C"), 
		NOT_ACCEPTED("#E59866"), 
		NOT_STARTED("#DDA520"), 
		IN_ACTIVE("#a8adab"), 
		PARTICIPATED("#8b7b8b"),
		ENDED_SESSION("#b9c170"),
		DEFAULT("#566666");

		private ColorWithStatus(String strValue) {
			this.strValue = strValue;
		}

		private final String strValue;

		public String getStrValue() {
			return strValue;
		}

	}

		public static enum ApprovalStatus {
			DENY("Deny", 3), ACCEPT("Accept", 2), WAITING("Waiting", 1);
	
			private ApprovalStatus(String strValue, int value) {
				this.strValue = strValue;
				this.value = value;
			}
	
			private final int value;
			private final String strValue;
	
			public int getValue() {
				return value;
			}
	
			public String getStrValue() {
				return strValue;
			}
		}

	public static enum QuizStatus {
		NOT_TAKEN_YET("Not taken yet", 0), AVAILABLE("Available", 1), TESTED("Tested %s times", 2);

		private QuizStatus(String strValue, Integer value) {
			this.strValue = strValue;
			this.value = value;
		}

		private final Integer value;
		private final String strValue;

		public Integer getValue() {
			return value;
		}

		public String getStrValue() {
			return strValue;
		}
	}
	
	public static enum QuizResultStatus {
        NOT_TAKEN_YET("Not taken yet", 0), FAILED("Failed", 1), PASSED("Passed", 2);

        private QuizResultStatus(String strValue, Integer value) {
            this.strValue = strValue;
            this.value = value;
        }

        private final Integer value;
        private final String strValue;

        public Integer getValue() {
            return value;
        }

        public String getStrValue() {
            return strValue;
        }
    }
	
	public static enum SubmitStatus {
		SUBMIT("Submit", 1), RESUBMIT("Manager Deines", 2), COMPLETED("Manager accept", 3);

		private SubmitStatus(String strValue, Integer value) {
			this.strValue = strValue;
			this.value = value;
		}

		private final String strValue;
		private final Integer value;

		public String getStrValue() {
			return strValue;
		}

		public Integer getValue() {
			return value;
		}
	}
	
	public static enum TrainingActionStatus{
	    SUBMITTED("Submitted", 0), DENIED("Manager Denied", 1), ACCEPTED("Manager Accepted", 2);
	    
	    private final String strValue;
	    private final Integer value;
	    
	    private TrainingActionStatus(String strValue, Integer value){
	        this.strValue = strValue;
	        this.value = value;
	    }

        public String getStrValue() {
            return strValue;
        }

        public Integer getValue() {
            return value;
        }
	}
	
	public static enum QuestionTypesForExport {
	    TEXT_BOX(1, "Text box"), RADIO(2, "Radio"), CHECKBOX(3, "Check box"), 
	    CHECKBOX_OTHER(4, "Check box has other"), COMMENT(5, "Comment"), EMAIL(6, "Email"), 
	    DROP_DOWN_LIST(7, "Drop down"), MATRIX(11, "Matrix");
	    
	    private QuestionTypesForExport(Integer value, String strValue) {
            this.strValue = strValue;
            this.value = value;
        }

        private final String strValue;
        private final Integer value;

        public String getStrValue() {
            return strValue;
        }

        public Integer getValue() {
            return value;
        }
	}
	
	public static enum QuestionTypes {
        TEXT_BOX(1, "text"), RADIO(2, "radiogroup"), CHECKBOX(3, "checkbox"), 
        CHECKBOX_OTHER(4, "checkboxother"), COMMENT(5, "comment"), EMAIL(6, "email"), 
        DROP_DOWN_LIST(7, "dropdown"), MATRIX(11, "matrix");
        
        private QuestionTypes(Integer value, String strValue) {
            this.strValue = strValue;
            this.value = value;
        }

        private final String strValue;
        private final Integer value;

        public String getStrValue() {
            return strValue;
        }

        public Integer getValue() {
            return value;
        }
    }
	
	public static enum Roles {
	    ADMIN("ROLE_ADMIN", 1), APPROVAL_MANAGER("ROLE_AM", 2), CODINATOR("ROLE_CODINATOR", 3),
	    TRAINEE("ROLE_TRAINEE", 4), HOD("ROLE_HOD", 5), HR("ROLE_HR", 6);
	    
	    private String roleName;
	    private int intValue;
	    
	    private Roles(String roleName, int intValue){
	        this.roleName = roleName;
	        this.intValue = intValue;
	    }
	    
	    public String getROLE_NAME(){
	        return roleName;
	    }
	    
	    public int getIntValue(){
	        return intValue;
	    }
	}
	
	public static enum PostSurveyStatus {
	    SUBMITTED("Submitted"), NOT_SUBMMITTED_YET("Not Submitted Yet");
	    
	    private String strValue;
	    
	    private PostSurveyStatus(String strValue){
	        this.strValue = strValue;
	    }
	    
	    public String getStrValue(){
	        return strValue;
	    }
	}
	
	public static enum TestStatus {
        NOT_TAKEN_YET("Not taken yet", 0), TESTED("Tested %s times", 1);

        private final Integer intValue;
        private final String strValue;
        
        private TestStatus(String strValue, Integer intValue) {
            this.strValue = strValue;
            this.intValue = intValue;
        }

        public Integer getIntValue() {
            return intValue;
        }

        public String getStrValue() {
            return strValue;
        }
    }
    
    public static enum TestResultStatus {
        NOT_TAKEN_YET("Not taken yet", 0), FAILED("Failed", 1), PASSED("Passed", 2);

        private final Integer value;
        private final String strValue;

        private TestResultStatus(String strValue, Integer value) {
            this.strValue = strValue;
            this.value = value;
        }

        public Integer getIntValue() {
            return value;
        }

        public String getStrValue() {
            return strValue;
        }
    }
}
