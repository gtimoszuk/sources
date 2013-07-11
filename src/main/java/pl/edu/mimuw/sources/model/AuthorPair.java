package pl.edu.mimuw.sources.model;

public class AuthorPair {

	private String firstAuthor;

	private String secondAuthor;

	public String getFirstAuthor() {
		return firstAuthor;
	}

	public String getSecondAuthor() {
		return secondAuthor;
	}

	public AuthorPair(String authorName1, String authorName2) {
		if (authorName1.compareTo(authorName2) > 0) {
			firstAuthor = authorName1;
			secondAuthor = authorName2;
		} else {
			firstAuthor = authorName2;
			secondAuthor = authorName1;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((firstAuthor == null) ? 0 : firstAuthor.hashCode());
		result = prime * result + ((secondAuthor == null) ? 0 : secondAuthor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthorPair other = (AuthorPair) obj;
		if (firstAuthor == null) {
			if (other.firstAuthor != null)
				return false;
		} else if (!firstAuthor.equals(other.firstAuthor))
			return false;
		if (secondAuthor == null) {
			if (other.secondAuthor != null)
				return false;
		} else if (!secondAuthor.equals(other.secondAuthor))
			return false;
		return true;
	}

}
