package com.dci.intellij.dbn.object.type;

import com.dci.intellij.dbn.common.Icons;
import com.dci.intellij.dbn.common.content.DynamicContentType;
import com.dci.intellij.dbn.common.util.StringUtil;
import com.dci.intellij.dbn.database.DatabaseObjectTypeId;
import com.dci.intellij.dbn.ddl.DDLFileTypeId;
import com.dci.intellij.dbn.editor.DBContentType;
import gnu.trove.THashSet;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

public enum DBObjectType implements DynamicContentType<DBObjectType> {
    
    ATTRIBUTE(DatabaseObjectTypeId.ATTRIBUTE, "attribute", "attribute", Icons.DBO_ATTRIBUTE, null, Icons.DBO_ATTRIBUTES, false),
    ARGUMENT(DatabaseObjectTypeId.ARGUMENT, "argument", "arguments", Icons.DBO_ARGUMENT, null, Icons.DBO_ARGUMENTS, false),
    CATEGORY(DatabaseObjectTypeId.CATEGORY, "category", "categories", null, null, null, false),
    CHARSET(DatabaseObjectTypeId.CHARSET, "charset", "charsets", null, null, null, false),
    CLUSTER(DatabaseObjectTypeId.CLUSTER, "cluster", "clusters", Icons.DBO_CLUSTER, null, Icons.DBO_CLUSTERS, false),
    COLUMN(DatabaseObjectTypeId.COLUMN, "column", "columns", Icons.DBO_COLUMN, null, Icons.DBO_COLUMNS, false),
    CONSTRAINT(DatabaseObjectTypeId.CONSTRAINT, "constraint", "constraints", Icons.DBO_CONSTRAINT, Icons.DBO_CONSTRAINT_DISABLED, Icons.DBO_CONSTRAINTS, false),
    DATABASE(DatabaseObjectTypeId.DATABASE, "database", "databases", null, null, null, false),
    DATASET(DatabaseObjectTypeId.DATASET, "dataset", "datasets", null, null, null, true),
    DIRECTORY(DatabaseObjectTypeId.DIRECTORY, "directory", "directories", null, null, null, true),
    DBLINK(DatabaseObjectTypeId.DBLINK, "dblink", "database links", Icons.DBO_DATABASE_LINK, null, Icons.DBO_DATABASE_LINKS, false),
    DIMENSION(DatabaseObjectTypeId.DIMENSION, "dimension", "dimensions", Icons.DBO_DIMENSION, null, Icons.DBO_DIMENSIONS, false),
    DIMENSION_ATTRIBUTE(DatabaseObjectTypeId.DIMENSION_ATTRIBUTE, "dimension attribute", "dimension attributes", null, null, null, false),
    DIMENSION_HIERARCHY(DatabaseObjectTypeId.DIMENSION_HIERARCHY, "dimension hierarchy", "dimension hierarchies", null, null, null, false),
    DIMENSION_LEVEL(DatabaseObjectTypeId.DIMENSION_LEVEL, "dimension level", "dimension levels", null, null, null, false),
    DISKGROUP(DatabaseObjectTypeId.DISKGROUP, "diskgroup", "diskgroups", null, null, null, false),
    DOMAIN(DatabaseObjectTypeId.DOMAIN, "domain", "domains", null, null, null, false),
    EDITION(DatabaseObjectTypeId.EDITION, "edition", "editions", null, null, null, false),
    FUNCTION(DatabaseObjectTypeId.FUNCTION, "function", "functions", Icons.DBO_FUNCTION, null, Icons.DBO_FUNCTIONS, false),
    GRANTED_ROLE(DatabaseObjectTypeId.GRANTED_ROLE, "granted role", "granted roles", Icons.DBO_ROLE, null, Icons.DBO_ROLES, false),
    GRANTED_PRIVILEGE(DatabaseObjectTypeId.GRANTED_PRIVILEGE, "granted privilege", "granted privileges", Icons.DBO_PRIVILEGE, null, Icons.DBO_PRIVILEGES, false),
    INDEX(DatabaseObjectTypeId.INDEX, "index", "indexes", Icons.DBO_INDEX, Icons.DBO_INDEX_DISABLED, Icons.DBO_INDEXES, false),
    INDEXTYPE(DatabaseObjectTypeId.INDEXTYPE, "indextype", "indextypes", null, null, null, false),
    JAVA_OBJECT(DatabaseObjectTypeId.JAVA_OBJECT, "java object", "java objects", null, null, null, false),
    JAVA_CLASS(DatabaseObjectTypeId.JAVA_CLASS, "java class", "java classes", null, null, null, false),
    LOB(DatabaseObjectTypeId.LOB, "lob", "lobs", null, null, null, false),
    MATERIALIZED_VIEW(DatabaseObjectTypeId.MATERIALIZED_VIEW, "materialized view", "materialized views", Icons.DBO_MATERIALIZED_VIEW, null, Icons.DBO_MATERIALIZED_VIEWS, false),
    METHOD(DatabaseObjectTypeId.METHOD, "method", "methods", null, null, null, true),
    MODEL(DatabaseObjectTypeId.MODEL, "model", "models", null, null, null, false),
    NESTED_TABLE(DatabaseObjectTypeId.NESTED_TABLE, "nested table", "nested tables", Icons.DBO_NESTED_TABLE, null, Icons.DBO_NESTED_TABLES, false),
    NESTED_TABLE_COLUMN(DatabaseObjectTypeId.NESTED_TABLE_COLUMN, "nested table column", "nested table columns", null, null, null, false),
    OPERATOR(DatabaseObjectTypeId.OPERATOR, "operator", "operators", null, null, null, false),
    OUTLINE(DatabaseObjectTypeId.OUTLINE, "outline", "outlines", null, null, null, false),
    PACKAGE(DatabaseObjectTypeId.PACKAGE, "package", "packages", Icons.DBO_PACKAGE, null, Icons.DBO_PACKAGES, false),
    PACKAGE_BODY(DatabaseObjectTypeId.PACKAGE_BODY, "package body", "package bodies", Icons.DBO_PACKAGE, null, Icons.DBO_PACKAGES, false),
    PACKAGE_FUNCTION(DatabaseObjectTypeId.PACKAGE_FUNCTION, "package function", "functions", Icons.DBO_FUNCTION, null, Icons.DBO_FUNCTIONS, false),
    PACKAGE_PROCEDURE(DatabaseObjectTypeId.PACKAGE_PROCEDURE, "package procedure", "procedures", Icons.DBO_PROCEDURE, null, Icons.DBO_PROCEDURES, false),
    PACKAGE_TYPE(DatabaseObjectTypeId.PACKAGE_TYPE, "package type", "types", Icons.DBO_TYPE, null, Icons.DBO_TYPES, false),
    PARTITION(DatabaseObjectTypeId.PARTITION, "partition", "partitions", null, null, null, false),
    PRIVILEGE(DatabaseObjectTypeId.PRIVILEGE, "privilege", "privileges", Icons.DBO_PRIVILEGE, null, Icons.DBO_PRIVILEGES, false),
    SYSTEM_PRIVILEGE(DatabaseObjectTypeId.SYSTEM_PRIVILEGE, "system privilege", "system privileges", Icons.DBO_PRIVILEGE, null, Icons.DBO_PRIVILEGES, false),
    OBJECT_PRIVILEGE(DatabaseObjectTypeId.OBJECT_PRIVILEGE, "object privilege", "object privileges", Icons.DBO_PRIVILEGE, null, Icons.DBO_PRIVILEGES, false),
    PROCEDURE(DatabaseObjectTypeId.PROCEDURE, "procedure", "procedures", Icons.DBO_PROCEDURE, null, Icons.DBO_PROCEDURES, false),
    PROGRAM(DatabaseObjectTypeId.PROGRAM, "program", "programs", null, null, null, true),
    PROFILE(DatabaseObjectTypeId.PROFILE, "profile", "profiles", null, null, null, false),
    ROLLBACK_SEGMENT(DatabaseObjectTypeId.ROLLBACK_SEGMENT, "rollback segment", "rollback segments", null, null, null, false),
    ROLE(DatabaseObjectTypeId.ROLE, "role", "roles", Icons.DBO_ROLE, null, Icons.DBO_ROLES, false),
    SCHEMA(DatabaseObjectTypeId.SCHEMA, "schema", "schemas", Icons.DBO_SCHEMA, null, Icons.DBO_SCHEMAS, false),
    SEQUENCE(DatabaseObjectTypeId.SEQUENCE, "sequence", "sequences", Icons.DBO_SEQUENCE, null, Icons.DBO_SEQUENCES, false),
    SUBPARTITION(DatabaseObjectTypeId.SUBPARTITION, "subpartition", "subpartitions", null, null, null, false),
    SYNONYM(DatabaseObjectTypeId.SYNONYM, "synonym", "synonyms", Icons.DBO_SYNONYM, null, Icons.DBO_SYNONYMS, false),
    TABLE(DatabaseObjectTypeId.TABLE, "table", "tables", Icons.DBO_TABLE, null, Icons.DBO_TABLES, false),
    TABLESPACE(DatabaseObjectTypeId.TABLESPACE, "tablespace", "tablespaces", null, null, null, false),
    TRIGGER(DatabaseObjectTypeId.TRIGGER, "trigger", "triggers", Icons.DBO_TRIGGER, Icons.DBO_TRIGGER_DISABLED, Icons.DBO_TRIGGERS, false),
    DATASET_TRIGGER(DatabaseObjectTypeId.DATASET_TRIGGER, "dataset trigger", "triggers", Icons.DBO_TRIGGER, Icons.DBO_TRIGGER_DISABLED, Icons.DBO_TRIGGERS, false),
    DATABASE_TRIGGER(DatabaseObjectTypeId.DATABASE_TRIGGER, "database trigger", "triggers", Icons.DBO_DATABASE_TRIGGER, Icons.DBO_DATABASE_TRIGGER_DISABLED, Icons.DBO_DATABASE_TRIGGERS, false),
    TYPE(DatabaseObjectTypeId.TYPE, "type", "types", Icons.DBO_TYPE, null, Icons.DBO_TYPES, false),
    TYPE_BODY(DatabaseObjectTypeId.TYPE_BODY, "type body", "type bodies", Icons.DBO_TYPE, null, Icons.DBO_TYPES, false),
    XMLTYPE(DatabaseObjectTypeId.XMLTYPE, "type", "types", Icons.DBO_TYPE, null, Icons.DBO_TYPES, false),
    TYPE_ATTRIBUTE(DatabaseObjectTypeId.TYPE_ATTRIBUTE, "type attribute", "attributes", Icons.DBO_ATTRIBUTE, null, Icons.DBO_ATTRIBUTES, false),
    TYPE_FUNCTION(DatabaseObjectTypeId.TYPE_FUNCTION, "type function", "functions", Icons.DBO_FUNCTION, null, Icons.DBO_FUNCTIONS, false),
    TYPE_PROCEDURE(DatabaseObjectTypeId.TYPE_PROCEDURE, "type procedure", "procedures", Icons.DBO_PROCEDURE, null, Icons.DBO_PROCEDURES, false),
    USER(DatabaseObjectTypeId.USER, "user", "users", Icons.DBO_USER, null, Icons.DBO_USERS, false),
    VARRAY(DatabaseObjectTypeId.VARRAY, "varray", "varrays", null, null, null, false),
    VARRAY_TYPE(DatabaseObjectTypeId.VARRAY_TYPE, "varray type", "varray types", null, null, null, false),
    VIEW(DatabaseObjectTypeId.VIEW, "view", "views", Icons.DBO_VIEW, null, Icons.DBO_VIEWS, false),

    CURSOR(DatabaseObjectTypeId.CURSOR, "cursor", "cursors", Icons.DBO_CURSOR, null, null, false),
    RECORD(DatabaseObjectTypeId.RECORD, "record", "records", null, null, null, false),
    PROPERTY(DatabaseObjectTypeId.PROPERTY, "property", "properties", null, null, null, false),
    JAVA(DatabaseObjectTypeId.JAVA, "java", "java", null, null, null, false),
    JAVA_LIB(DatabaseObjectTypeId.JAVA_LIB, "java library", "java libraries", null, null, null, false),
    PARAMETER(DatabaseObjectTypeId.PARAMETER, "parameter", "parameters", null, null, null, false),
    EXCEPTION(DatabaseObjectTypeId.EXCEPTION, "exception", "exceptions", null, null, null, false),
    SAVEPOINT(DatabaseObjectTypeId.SAVEPOINT, "savepoint", "savepoints", null, null, null, false),
    LABEL(DatabaseObjectTypeId.LABEL, "label", "labels", null, null, null, false),
    WINDOW(DatabaseObjectTypeId.WINDOW, "window", "windows", null, null, null, false),

    CONSOLE(DatabaseObjectTypeId.CONSOLE, "console", "consoles", Icons.DBO_CONSOLE, null, Icons.DBO_CONSOLES, false),
    UNKNOWN(DatabaseObjectTypeId.UNKNOWN, "unknown", null, null, null, null, true),
    NONE(DatabaseObjectTypeId.NONE, "none", null, null, null, null, true),
    ANY(DatabaseObjectTypeId.ANY, "any", "dependencies", null, null, null, true),

    // from oracle synonym to dropped object (??)
    NON_EXISTENT(DatabaseObjectTypeId.NON_EXISTENT, "non-existent", null, null, null, null, true),

    INCOMING_DEPENDENCY(DatabaseObjectTypeId.INCOMING_DEPENDENCY, "incoming dependency", "incoming dependencies", null, null, null, true),
    OUTGOING_DEPENDENCY(DatabaseObjectTypeId.INCOMING_DEPENDENCY, "outgoing dependency", "outgoing dependencies", null, null, null, true);

    private DatabaseObjectTypeId typeId;
    private String name;
    private String listName;
    private String presentableListName;
    private Icon icon;
    private Icon disabledIcon;
    private Icon listIcon;
    private boolean generic;

    private DBObjectType genericType;
    private DBContentType contentType = DBContentType.NONE;
    private Set<DBObjectType> parents = new THashSet<DBObjectType>();
    private Set<DBObjectType> genericParents = new THashSet<DBObjectType>();
    private Set<DBObjectType> children = new THashSet<DBObjectType>();
    private Set<DBObjectType> inheritingTypes = new THashSet<DBObjectType>();
    private Set<DBObjectType> familyTypes;
    private Set<DBObjectType> thisAsSet = new THashSet<DBObjectType>();

    private Map<DBContentType, Icon> icons;
    private Map<DBContentType, DDLFileTypeId> ddlFileTypeIds;

    DBObjectType(DatabaseObjectTypeId typeId, String name, String listName, Icon icon, Icon disabledIcon, Icon listIcon, boolean generic) {
        this.typeId = typeId;
        this.name = name;
        this.listName = listName;
        this.icon = icon;
        this.listIcon = listIcon;
        this.disabledIcon = disabledIcon;
        this.generic = generic;
        this.presentableListName = listName == null ? null :
                Character.toUpperCase(listName.charAt(0)) + listName.substring(1).replace('_', ' ');
        thisAsSet.add(this);
    }

    public boolean isSchemaObject() {
        return parents.contains(SCHEMA);
    }

    public DBContentType getContentType() {
        return contentType;
    }

    public DatabaseObjectTypeId getTypeId() {
        return typeId;
    }

    public String getName() {
        return name;
    }

    public String getListName() {
        return listName;
    }

    public String getPresentableListName() {
        return presentableListName;
    }

    public Icon getIcon() {
        return icon;
    }

    public Icon getIcon(DBContentType contentType) {
        Icon icon = null;
        if (icons != null) {
            icon = icons.get(contentType);
        }
        return icon == null ?  this.icon : icon;
    }

    public Icon getDisabledIcon() {
        return disabledIcon != null ? disabledIcon : icon;
    }

    public Icon getListIcon() {
        return listIcon;
    }

    public boolean isGeneric() {
        return generic;
    }

    public boolean isLeaf() {
        return children.isEmpty();
    }

    public Set<DBObjectType> getParents() {
        return parents;
    }

    public Set<DBObjectType> getGenericParents() {
        return genericParents;
    }

    public Set<DBObjectType> getChildren() {
        return children;
    }

    public Set<DBObjectType> getInheritingTypes() {
        return inheritingTypes;
    }

    public Set<DBObjectType> getFamilyTypes() {
        if (familyTypes == null) {
            familyTypes = new HashSet<DBObjectType>();
            familyTypes.addAll(inheritingTypes);
            familyTypes.add(this);
        }
        return familyTypes;
    }

    @Override
    public DBObjectType getGenericType() {
        return genericType == null ? this : genericType.getGenericType();
    }

    @Nullable
    public DDLFileTypeId getDdlFileTypeId(@Nullable DBContentType contentType) {
        return ddlFileTypeIds == null ? null : ddlFileTypeIds.get(contentType);
    }

    @Nullable
    public Collection<DDLFileTypeId> getDdlFileTypeIds() {
        return ddlFileTypeIds == null ? null : ddlFileTypeIds.values();
    }

    public boolean isInheriting(DBObjectType objectType) {
        return objectType.inheritingTypes.contains(this);
    }

    public boolean isParentOf(DBObjectType objectType) {
        return objectType.parents.contains(this) || objectType.genericParents.contains(this);
    }

    public boolean isChildOf(DBObjectType objectType) {
        return objectType.children.contains(this);
    }

    public boolean hasChild(DBObjectType objectType) {
        for (DBObjectType childObjectType : children) {
            if (childObjectType.matches(objectType)) {
                return true;
            }
        }
        return false;
    }


    public boolean matchesOneOf(DBObjectType ... objectTypes) {
        for (DBObjectType objectType : objectTypes) {
            if (matches(objectType)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean matches(DBObjectType objectType) {
        if (this == ANY || objectType == ANY) {
            return true;
        }

        DBObjectType thisObjectType = this;
        while (thisObjectType != null) {
            if (thisObjectType == objectType) return true;
            thisObjectType = thisObjectType.genericType;
        }

        DBObjectType thatObjectType = objectType.genericType;
        while (thatObjectType != null) {
            if (thatObjectType == this) return true;
            thatObjectType = thatObjectType.genericType;
        }

        return false;
    }

    public boolean isOneOf(DBObjectType ... objectTypes) {
        for (DBObjectType objectType : objectTypes) {
            if (objectType.matches(this)) {
                return true;
            }
        }

        return false;
    }

    static {
        // Generic type
        TABLE.setGenericType(DATASET);
        VIEW.setGenericType(DATASET);
        CURSOR.setGenericType(DATASET);
        MATERIALIZED_VIEW.setGenericType(DATASET);
        PROCEDURE.setGenericType(METHOD);
        FUNCTION.setGenericType(METHOD);
        TYPE.setGenericType(PROGRAM);
        TYPE_PROCEDURE.setGenericType(PROCEDURE);
        TYPE_FUNCTION.setGenericType(FUNCTION);
        TYPE_ATTRIBUTE.setGenericType(ATTRIBUTE);
        PACKAGE.setGenericType(PROGRAM);
        PACKAGE_PROCEDURE.setGenericType(PROCEDURE);
        PACKAGE_FUNCTION.setGenericType(FUNCTION);
        PACKAGE_TYPE.setGenericType(TYPE);
        DATASET_TRIGGER.setGenericType(TRIGGER);
        DATABASE_TRIGGER.setGenericType(TRIGGER);
        XMLTYPE.setGenericType(TYPE);

        SYSTEM_PRIVILEGE.setGenericType(PRIVILEGE);
        OBJECT_PRIVILEGE.setGenericType(PRIVILEGE);
        GRANTED_PRIVILEGE.setGenericType(PRIVILEGE);
        GRANTED_ROLE.setGenericType(ROLE);

        // Parent relations
        ARGUMENT.addParent(FUNCTION);
        ARGUMENT.addParent(PROCEDURE);
        ARGUMENT.addParent(METHOD);
        ARGUMENT.addParent(PACKAGE_FUNCTION);
        ARGUMENT.addParent(PACKAGE_PROCEDURE);
        CLUSTER.addParent(SCHEMA);
        COLUMN.addParent(DATASET);
        COLUMN.addParent(TABLE);
        COLUMN.addParent(VIEW);
        COLUMN.addParent(CURSOR);
        COLUMN.addParent(MATERIALIZED_VIEW);
        CONSTRAINT.addParent(SCHEMA);
        CONSTRAINT.addParent(DATASET);
        CONSTRAINT.addParent(TABLE);
        CONSTRAINT.addParent(VIEW);
        CONSTRAINT.addParent(MATERIALIZED_VIEW);
        DATASET.addParent(SCHEMA);
        DBLINK.addParent(SCHEMA);
        DIMENSION.addParent(SCHEMA);
        FUNCTION.addParent(SCHEMA);
        FUNCTION.addParent(PACKAGE);
        DIMENSION_ATTRIBUTE.addParent(DIMENSION);
        DIMENSION_HIERARCHY.addParent(DIMENSION);
        DIMENSION_LEVEL.addParent(DIMENSION);
        INDEX.addParent(SCHEMA);
        MATERIALIZED_VIEW.addParent(SCHEMA);
        NESTED_TABLE.addParent(TABLE);
        NESTED_TABLE_COLUMN.addParent(NESTED_TABLE);
        PACKAGE.addParent(SCHEMA);
        PACKAGE_BODY.addParent(SCHEMA);
        PACKAGE_FUNCTION.addParent(PACKAGE);
        PACKAGE_PROCEDURE.addParent(PACKAGE);
        PACKAGE_TYPE.addParent(PACKAGE);
        PROCEDURE.addParent(SCHEMA);
        PROCEDURE.addParent(PACKAGE);
        METHOD.addParent(SCHEMA);
        METHOD.addParent(PACKAGE);
        SEQUENCE.addParent(SCHEMA);
        SYNONYM.addParent(SCHEMA);
        TABLE.addParent(SCHEMA);
        TRIGGER.addParent(SCHEMA);
        TRIGGER.addParent(DATASET);
        TRIGGER.addParent(TABLE);
        TRIGGER.addParent(VIEW);
        TRIGGER.addParent(MATERIALIZED_VIEW);
        DATASET_TRIGGER.addParent(SCHEMA);
        DATASET_TRIGGER.addParent(DATASET);
        DATASET_TRIGGER.addParent(TABLE);
        DATASET_TRIGGER.addParent(VIEW);
        DATASET_TRIGGER.addParent(MATERIALIZED_VIEW);
        DATABASE_TRIGGER.addParent(SCHEMA);
        TYPE.addParent(SCHEMA);
        TYPE_FUNCTION.addParent(TYPE);
        TYPE_PROCEDURE.addParent(TYPE);
        TYPE_ATTRIBUTE.addParent(TYPE);
        TYPE_FUNCTION.addParent(PACKAGE_TYPE);
        TYPE_PROCEDURE.addParent(PACKAGE_TYPE);
        TYPE_ATTRIBUTE.addParent(PACKAGE_TYPE);
        TYPE_ATTRIBUTE.addParent(PACKAGE_TYPE);
        VIEW.addParent(SCHEMA);

        PACKAGE.addIcon(DBContentType.CODE_SPEC, Icons.DBO_PACKAGE_SPEC);
        PACKAGE.addIcon(DBContentType.CODE_BODY, Icons.DBO_PACKAGE_BODY);

        INCOMING_DEPENDENCY.setGenericType(ANY);
        OUTGOING_DEPENDENCY.setGenericType(ANY);

        // Content types
        FUNCTION.contentType = DBContentType.CODE;
        PROCEDURE.contentType = DBContentType.CODE;
        TABLE.contentType = DBContentType.DATA;
        VIEW.contentType = DBContentType.CODE_AND_DATA;
        MATERIALIZED_VIEW.contentType = DBContentType.CODE_AND_DATA;
        TYPE.contentType = DBContentType.CODE_SPEC_AND_BODY;
        PACKAGE.contentType = DBContentType.CODE_SPEC_AND_BODY;
        TRIGGER.contentType = DBContentType.CODE;
        DATASET_TRIGGER.contentType = DBContentType.CODE;
        DATABASE_TRIGGER.contentType = DBContentType.CODE;


        // DDL file types
        VIEW.addDdlFileType(DBContentType.CODE, DDLFileTypeId.VIEW);
        MATERIALIZED_VIEW.addDdlFileType(DBContentType.CODE, DDLFileTypeId.VIEW);
        TRIGGER.addDdlFileType(DBContentType.CODE, DDLFileTypeId.TRIGGER);
        DATASET_TRIGGER.addDdlFileType(DBContentType.CODE, DDLFileTypeId.TRIGGER);
        DATABASE_TRIGGER.addDdlFileType(DBContentType.CODE, DDLFileTypeId.TRIGGER);
        FUNCTION.addDdlFileType(DBContentType.CODE, DDLFileTypeId.FUNCTION);
        PROCEDURE.addDdlFileType(DBContentType.CODE, DDLFileTypeId.PROCEDURE);

        TYPE.addDdlFileType(DBContentType.CODE_SPEC_AND_BODY, DDLFileTypeId.TYPE);
        TYPE.addDdlFileType(DBContentType.CODE_SPEC, DDLFileTypeId.TYPE_SPEC);
        TYPE.addDdlFileType(DBContentType.CODE_BODY, DDLFileTypeId.TYPE_BODY);

        PACKAGE.addDdlFileType(DBContentType.CODE_SPEC_AND_BODY, DDLFileTypeId.PACKAGE);
        PACKAGE.addDdlFileType(DBContentType.CODE_SPEC, DDLFileTypeId.PACKAGE_SPEC);
        PACKAGE.addDdlFileType(DBContentType.CODE_BODY, DDLFileTypeId.PACKAGE_BODY);

    }

    /*************************************************************************
     *                   Static lookup utilities                             *
     *************************************************************************/
    public static DBObjectType get(DatabaseObjectTypeId typeId) {
        for (DBObjectType objectType: values()) {
            if (objectType.typeId == typeId) {
                return objectType;
            }
        }
        System.out.println("ERROR - [UNKNOWN] undefined object type: " + typeId);
        return UNKNOWN;
    }

    public static DBObjectType get(String typeName, DBObjectType defaultObjectType) {
        DBObjectType objectType = get(typeName);
        return objectType == UNKNOWN ? defaultObjectType : objectType;
    }

    public static DBObjectType get(String typeName) {
        if (StringUtil.isEmpty(typeName)) {
            return null;
        }

        try {
            return valueOf(typeName);
        } catch (IllegalArgumentException e) {
            typeName = typeName.replace('_', ' ');
            for (DBObjectType objectType: values()) {
                if (StringUtil.equalsIgnoreCase(objectType.name, typeName)) {
                    return objectType;
                }
            }
            System.out.println("ERROR - [UNKNOWN] undefined object type: " + typeName);
            return UNKNOWN;
        }
    }

    public static String toCsv(List<DBObjectType> objectTypes) {
        StringBuilder buffer = new StringBuilder();
        for (DBObjectType objectType : objectTypes) {
            if (buffer.length() != 0) buffer.append(", ");
            buffer.append(objectType.name);
        }
        return buffer.toString();
    }

    public static List<DBObjectType> fromCsv(String objectTypes) {
        List<DBObjectType> list = new ArrayList<DBObjectType>();
        StringTokenizer tokenizer = new StringTokenizer(objectTypes, ",");
        while (tokenizer.hasMoreTokens()) {
            String objectTypeName = tokenizer.nextToken().trim();
            list.add(DBObjectType.get(objectTypeName));
        }
        return list;
    }

    public static DBObjectType forName(String name) {
        for (DBObjectType objectType : values()) {
            if (objectType.getName().equals(name)) {
                return objectType;
            }
        }
        throw new IllegalArgumentException("No ObjectType found for name '" + name + "'");
    }

    public static DBObjectType forListName(String name, DBObjectType parent) {
        for (DBObjectType objectType : values()) {
            if (objectType.getListName().equals(name) && (parent == null || objectType.getParents().contains(parent))) {
                return objectType;
            }
        }
        if (parent != null) {
            return forListName(name, null);
        }

        throw new IllegalArgumentException("No ObjectType found for actions name '" + name + "'");
    }

    /*************************************************************************
     *                   Initialization utilities                             *
     *************************************************************************/
    private void addIcon(DBContentType contentType, Icon icon) {
        if (icons == null) {
            icons = new EnumMap<>(DBContentType.class);
        }
        icons.put(contentType, icon);
    }

    private void addDdlFileType(DBContentType contentType, DDLFileTypeId ddlFileType) {
        if (ddlFileTypeIds == null) {
            ddlFileTypeIds = new EnumMap<>(DBContentType.class);
        }
        ddlFileTypeIds.put(contentType, ddlFileType);
    }

    private void addParent(DBObjectType parent) {
        parents.add(parent);
        genericParents.add(parent.getGenericType());
        parent.children.add(this);
    }

    private void setGenericType(DBObjectType genericType) {
        this.genericType = genericType;
        genericType.inheritingTypes.add(this);
    }


}
