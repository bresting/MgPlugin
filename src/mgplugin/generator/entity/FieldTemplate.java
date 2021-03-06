package mgplugin.generator.entity;

/**
 * <pre>
 * @programName : 프로그램명
 * @description : 프로그램_처리내용
 * @history
 * ----------   ---------------   ------------------------------------------------------------------
 * 수정일       수정자            수정내용
 * ----------   ---------------   ------------------------------------------------------------------
 * 2019.12.03   김도진         최초생성
 *
 * </pre>
 */
public class FieldTemplate {

    // 필드셋 반복부 관련
    private String name;
    private String type;
    private String columnName;
    private String bindName;
    private String comment;
    private String pkYn;
    private String identityYn;
    
    private String nexaType;
    private String nexaSize;

    private String typeSpace       = " ";
    private String nameSpace       = " ";
    private String columnNameSpace = " ";
    private String commentSpace    = " ";
    
    private String pkNameSpace       = " ";
    private String pkColumnNameSpace = " ";
    private String pkCommentSpace    = " ";
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return the type
     */
    public String getType() {
        return type;
    }
    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the columnName
     */
    public String getColumnName() {
        return columnName;
    }
    /**
     * @param columnName the columnName to set
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
    /**
     * @return the bindName
     */
    public String getBindName() {
        return bindName;
    }
    /**
     * @param bindName the bindName to set
     */
    public void setBindName(String bindName) {
        this.bindName = bindName;
    }
    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }
    /**
     * @param comment the comment to set
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    /**
     * @return the pkYn
     */
    public String getPkYn() {
        return pkYn;
    }
    /**
     * @param pkYn the pkYn to set
     */
    public void setPkYn(String pkYn) {
        this.pkYn = pkYn;
    }
    /**
     * @return the identityYn
     */
    public String getIdentityYn() {
        return identityYn;
    }
    /**
     * @param identityYn the identityYn to set
     */
    public void setIdentityYn(String identityYn) {
        this.identityYn = identityYn;
    }
    /**
     * @return the nameSpace
     */
    public String getNameSpace() {
        return nameSpace;
    }
    /**
     * @param nameSpace the nameSpace to set
     */
    public void setNameSpace(String nameSpace) {
        this.nameSpace = nameSpace;
    }
    /**
     * @return the columnNameSpace
     */
    public String getColumnNameSpace() {
        return columnNameSpace;
    }
    /**
     * @param columnNameSpace the columnNameSpace to set
     */
    public void setColumnNameSpace(String columnNameSpace) {
        this.columnNameSpace = columnNameSpace;
    }
    /**
     * @return the commentSpace
     */
    public String getCommentSpace() {
        return commentSpace;
    }
    /**
     * @param commentSpace the commentSpace to set
     */
    public void setCommentSpace(String commentSpace) {
        this.commentSpace = commentSpace;
    }
    /**
     * @return the typeSpace
     */
    public String getTypeSpace() {
        return typeSpace;
    }
    /**
     * @param typeSpace the typeSpace to set
     */
    public void setTypeSpace(String typeSpace) {
        this.typeSpace = typeSpace;
    }
    /**
     * @return the pkNameSpace
     */
    public String getPkNameSpace() {
        return pkNameSpace;
    }
    /**
     * @param pkNameSpace the pkNameSpace to set
     */
    public void setPkNameSpace(String pkNameSpace) {
        this.pkNameSpace = pkNameSpace;
    }
    /**
     * @return the pkColumnNameSpace
     */
    public String getPkColumnNameSpace() {
        return pkColumnNameSpace;
    }
    /**
     * @param pkColumnNameSpace the pkColumnNameSpace to set
     */
    public void setPkColumnNameSpace(String pkColumnNameSpace) {
        this.pkColumnNameSpace = pkColumnNameSpace;
    }
    /**
     * @return the pkCommentSpace
     */
    public String getPkCommentSpace() {
        return pkCommentSpace;
    }
    /**
     * @param pkCommentSpace the pkCommentSpace to set
     */
    public void setPkCommentSpace(String pkCommentSpace) {
        this.pkCommentSpace = pkCommentSpace;
    }
    /**
     * @return the nexaType
     */
    public String getNexaType() {
        return nexaType;
    }
    /**
     * @param nexaType the nexaType to set
     */
    public void setNexaType(String nexaType) {
        this.nexaType = nexaType;
    }
    /**
     * @return the nexaSize
     */
    public String getNexaSize() {
        return nexaSize;
    }
    /**
     * @param nexaSize the nexaSize to set
     */
    public void setNexaSize(String nexaSize) {
        this.nexaSize = nexaSize;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "FieldTemplate [name=" + name + ", type=" + type + ", columnName=" + columnName + ", bindName="
                + bindName + ", comment=" + comment + ", pkYn=" + pkYn + ", identityYn=" + identityYn + ", nexaType="
                + nexaType + ", nexaSize=" + nexaSize + ", typeSpace=" + typeSpace + ", nameSpace=" + nameSpace
                + ", columnNameSpace=" + columnNameSpace + ", commentSpace=" + commentSpace + ", pkNameSpace="
                + pkNameSpace + ", pkColumnNameSpace=" + pkColumnNameSpace + ", pkCommentSpace=" + pkCommentSpace
                + ", getName()=" + getName() + ", getType()=" + getType() + ", getColumnName()=" + getColumnName()
                + ", getBindName()=" + getBindName() + ", getComment()=" + getComment() + ", getPkYn()=" + getPkYn()
                + ", getIdentityYn()=" + getIdentityYn() + ", getNameSpace()=" + getNameSpace()
                + ", getColumnNameSpace()=" + getColumnNameSpace() + ", getCommentSpace()=" + getCommentSpace()
                + ", getTypeSpace()=" + getTypeSpace() + ", getPkNameSpace()=" + getPkNameSpace()
                + ", getPkColumnNameSpace()=" + getPkColumnNameSpace() + ", getPkCommentSpace()=" + getPkCommentSpace()
                + ", getNexaType()=" + getNexaType() + ", getNexaSize()=" + getNexaSize() + ", getClass()=" + getClass()
                + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
    }
    
    
    
}
