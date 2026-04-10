# Sonar activated rules: java (PAX Sonar way)

| Rule Key | Name | Severity | Type |
|----------|------|----------|------|
| fb-contrib:ABC_ARRAY_BASED_COLLECTIONS | Correctness - Method uses array as basis of collection | MAJOR | BUG |
| fb-contrib:AIOB_ARRAY_INDEX_OUT_OF_BOUNDS | Correctness - Method attempts to access an array element outside the array's size | MAJOR | BUG |
| fb-contrib:AI_ANNOTATION_ISSUES_NEEDS_NULLABLE | Correctness - Method that can return null, is missing a @Nullable annotation | MAJOR | BUG |
| fb-contrib:AKI_SUPERFLUOUS_ROUTE_SPECIFICATION | Correctness - Method specifies superfluous routes thru route() or concat() | MAJOR | BUG |
| fb-contrib:AOM_ABSTRACT_OVERRIDDEN_METHOD | Correctness - Abstract method overrides a concrete implementation | MAJOR | BUG |
| fb-contrib:AWCBR_ARRAY_WRAPPED_CALL_BY_REFERENCE | Style - Method uses 1 element array to simulate call by reference | INFO | CODE_SMELL |
| fb-contrib:BAS_BLOATED_ASSIGNMENT_SCOPE | Performance - Method assigns a variable in a larger scope than is needed | MAJOR | BUG |
| fb-contrib:BED_BOGUS_EXCEPTION_DECLARATION | Correctness - Non derivable method declares throwing an exception that isn't thrown | MAJOR | BUG |
| fb-contrib:BED_HIERARCHICAL_EXCEPTION_DECLARATION | Correctness - Method declares throwing two or more exceptions related by inheritance | MAJOR | BUG |
| fb-contrib:BRPI_BACKPORT_REUSE_PUBLIC_IDENTIFIERS | Performance - Method uses backported libraries that are now built in | MAJOR | BUG |
| fb-contrib:BSB_BLOATED_SYNCHRONIZED_BLOCK | Performance - Method overly synchronizes a block of code | MAJOR | BUG |
| fb-contrib:CAAL_CONFUSING_ARRAY_AS_LIST | Correctness - Method calls Array.asList on an array of primitive values | MAJOR | BUG |
| fb-contrib:CAO_CONFUSING_AUTOBOXED_OVERLOADING | Correctness - Class defines methods which confuse Character with int parameters | MAJOR | BUG |
| fb-contrib:CBC_CONTAINS_BASED_CONDITIONAL | Style - This method uses an excessively complex conditional that can be replaced with Set.contains | INFO | CODE_SMELL |
| fb-contrib:CBX_CUSTOM_BUILT_XML | Style - Method builds XML strings through ad hoc concatenation | INFO | CODE_SMELL |
| fb-contrib:CCI_CONCURRENT_COLLECTION_ISSUES_USE_PUT_IS_RACY | Correctness - Method gets and sets a value of a ConcurrentHashMap in a racy manner | MAJOR | BUG |
| fb-contrib:CCNE_COMPARE_CLASS_EQUALS_NAME | Correctness - Method compares class name instead of comparing class | MAJOR | BUG |
| fb-contrib:CEBE_COMMONS_EQUALS_BUILDER_ISEQUALS | Correctness - Method returns the result of invoking equals() on EqualsBuilder | MAJOR | BUG |
| fb-contrib:CFS_CONFUSING_FUNCTION_SEMANTICS | Style - Method returns modified parameter | INFO | CODE_SMELL |
| fb-contrib:CHTH_COMMONS_HASHCODE_BUILDER_TOHASHCODE | Correctness - Method returns the result of invoking hashCode() on HashCodeBuilder | MAJOR | BUG |
| fb-contrib:CLI_CONSTANT_LIST_INDEX | Correctness - Method accesses list or array with constant index | MAJOR | BUG |
| fb-contrib:CNC_COLLECTION_NAMING_CONFUSION | Style - Collection variable is named with a different type of collection in the name | INFO | CODE_SMELL |
| fb-contrib:COM_COPIED_OVERRIDDEN_METHOD | Style - Method is implemented with an exact copy of its superclass' method | INFO | CODE_SMELL |
| fb-contrib:COM_PARENT_DELEGATED_CALL | Style - Method merely delegates to its superclass's version | INFO | CODE_SMELL |
| fb-contrib:CRF_CONFLATING_RESOURCES_AND_FILES | Correctness - This method accesses URL resources using the File API | MAJOR | BUG |
| fb-contrib:CSBTS_COMMONS_STRING_BUILDER_TOSTRING | Correctness - Method returns the result of invoking toString() without intermediate invocation of append() in ToStringBuilder | MAJOR | BUG |
| fb-contrib:CSI_CHAR_SET_ISSUES_UNKNOWN_ENCODING | Correctness - This method uses an unknown character encoding literal | MAJOR | BUG |
| fb-contrib:CSI_CHAR_SET_ISSUES_USE_STANDARD_CHARSET | Correctness - This method needlessly uses a String literal as a Charset encoding | MAJOR | BUG |
| fb-contrib:CSI_CHAR_SET_ISSUES_USE_STANDARD_CHARSET_NAME | Correctness - This method should use a StandardCharsets.XXX.name() to specify an encoding | MAJOR | BUG |
| fb-contrib:CTU_CONFLICTING_TIME_UNITS | Correctness - This method performs arithmetic operations on time values with different units | MAJOR | BUG |
| fb-contrib:CU_CLONE_USABILITY_MISMATCHED_RETURN | Style - Clone method declares it returns a type different than the owning class | INFO | CODE_SMELL |
| fb-contrib:CU_CLONE_USABILITY_OBJECT_RETURN | Style - Clone method declares it returns an Object | INFO | CODE_SMELL |
| fb-contrib:CU_CLONE_USABILITY_THROWS | Style - Clone method declares it throws CloneNotSupportedException | INFO | CODE_SMELL |
| fb-contrib:CVAA_CONTRAVARIANT_ARRAY_ASSIGNMENT | Correctness - Method performs a contravariant array assignment | MAJOR | BUG |
| fb-contrib:CVAA_CONTRAVARIANT_ELEMENT_ASSIGNMENT | Correctness - Method performs a contravariant array element assignment | MAJOR | BUG |
| fb-contrib:DDC_DOUBLE_DATE_COMPARISON | Performance - Method uses two date comparisons when one would do | MAJOR | BUG |
| fb-contrib:DMC_DUBIOUS_MAP_COLLECTION | Correctness - Class holds a map-type field, but uses it as only a List | MAJOR | BUG |
| fb-contrib:DSOC_DUBIOUS_SET_OF_COLLECTIONS | Performance - Method uses a set of collections | MAJOR | BUG |
| fb-contrib:DTEP_DEPRECATED_TYPESAFE_ENUM_PATTERN | Style - Class appears to implement the old style type safe enum pattern | INFO | CODE_SMELL |
| fb-contrib:DWI_DELETING_WHILE_ITERATING | Correctness - Method deletes collection element while iterating | MAJOR | BUG |
| fb-contrib:DWI_MODIFYING_WHILE_ITERATING | Correctness - Method modifies collection element while iterating | MAJOR | BUG |
| fb-contrib:ENMI_EQUALS_ON_ENUM | Correctness - Method calls equals on an enum instance | MAJOR | BUG |
| fb-contrib:ENMI_NULL_ENUM_VALUE | Correctness - Method sets an enum reference to null | MAJOR | BUG |
| fb-contrib:ENMI_ONE_ENUM_VALUE | Correctness - Enum class only declares one enum value | MAJOR | BUG |
| fb-contrib:EXS_EXCEPTION_SOFTENING_HAS_CHECKED | Style - Constrained method converts checked exception to unchecked instead of another allowable checked exception | INFO | CODE_SMELL |
| fb-contrib:EXS_EXCEPTION_SOFTENING_NO_CHECKED | Style - Constrained method converts checked exception to unchecked | INFO | CODE_SMELL |
| fb-contrib:FII_AVOID_CONTAINS_ON_COLLECTED_STREAM | Correctness - Method calls contains() on a collected lambda expression | MAJOR | BUG |
| fb-contrib:FII_AVOID_MAP_BEFORE_LIMIT | Correctness - Method calls limit() directly after calling map | MAJOR | BUG |
| fb-contrib:FII_AVOID_SIZE_ON_COLLECTED_STREAM | Correctness - Method calls size() on a collected lambda expression | MAJOR | BUG |
| fb-contrib:FII_COMBINE_FILTERS | Correctness - Method implements a stream using back to back filters | MAJOR | BUG |
| fb-contrib:FII_USE_ANY_MATCH | Correctness - Method suboptimally finds any match in a stream | MAJOR | BUG |
| fb-contrib:FII_USE_COPYCONSTRUCTOR | Correctness - Method calls collect() directly on a collection stream | MAJOR | BUG |
| fb-contrib:FII_USE_FIND_FIRST | Correctness - Method collects a List from a stream() just to get the first element | MAJOR | BUG |
| fb-contrib:FPL_FLOATING_POINT_LOOPS | Correctness - Method uses floating point indexed loops | MAJOR | BUG |
| fb-contrib:FP_FINAL_PARAMETERS | Style - Method does not define a parameter as final, but could | INFO | CODE_SMELL |
| fb-contrib:HCP_HTTP_REQUEST_RESOURCES_NOT_FREED_FIELD | Correctness - Unreleased HttpRequest network resources (field) | MAJOR | BUG |
| fb-contrib:HCP_HTTP_REQUEST_RESOURCES_NOT_FREED_LOCAL | Correctness - Unreleased HttpRequest network resources (local) | MAJOR | BUG |
| fb-contrib:HES_EXECUTOR_OVERWRITTEN_WITHOUT_SHUTDOWN | Correctness - An ExecutorService isn't shutdown before the reference to it is lost | MAJOR | BUG |
| fb-contrib:HES_LOCAL_EXECUTOR_SERVICE | Correctness - Suspicious Local Executor Service | MAJOR | BUG |
| fb-contrib:ICA_INVALID_CONSTANT_ARGUMENT | Correctness - Method passes an invalid value as a method argument | MAJOR | BUG |
| fb-contrib:IKNC_INCONSISTENT_HTTP_ATTRIBUTE_CASING | Style - Method uses the same HttpSession attribute name but with different casing | INFO | CODE_SMELL |
| fb-contrib:IKNC_INCONSISTENT_HTTP_PARAM_CASING | Style - Method uses the same HttpRequest parameter name but with different casing | INFO | CODE_SMELL |
| fb-contrib:IMC_IMMATURE_CLASS_BAD_SERIALVERSIONUID | Correctness - Class defines a computed serialVersionUID that doesn't equate to the calculated value | MAJOR | BUG |
| fb-contrib:IMC_IMMATURE_CLASS_COLLECTION_RETURN | Correctness - Method returns java.util.Collection | MAJOR | BUG |
| fb-contrib:IMC_IMMATURE_CLASS_IDE_GENERATED_PARAMETER_NAMES | Style - Method uses IDE generated parameter names | INFO | CODE_SMELL |
| fb-contrib:IMC_IMMATURE_CLASS_LOWER_CLASS | Style - Class does not start with an upper case letter | INFO | CODE_SMELL |
| fb-contrib:IMC_IMMATURE_CLASS_NO_EQUALS | Style - Class does not implement an equals method | INFO | CODE_SMELL |
| fb-contrib:IMC_IMMATURE_CLASS_NO_HASHCODE | Style - Class does not implement a hashCode method | INFO | CODE_SMELL |
| fb-contrib:IMC_IMMATURE_CLASS_NO_PACKAGE | Style - Class is defined in the default package | INFO | CODE_SMELL |
| fb-contrib:IMC_IMMATURE_CLASS_NO_TOSTRING | Style - Class does not implement a toString method | INFO | CODE_SMELL |
| fb-contrib:IMC_IMMATURE_CLASS_PRINTSTACKTRACE | Style - Method prints the stack trace to the console | INFO | CODE_SMELL |
| fb-contrib:IMC_IMMATURE_CLASS_UPPER_PACKAGE | Style - Class is defined in a package with upper case characters | INFO | CODE_SMELL |
| fb-contrib:IMC_IMMATURE_CLASS_VAR_NAME | Correctness - Class defines a field or local variable named 'var' | MAJOR | BUG |
| fb-contrib:IMC_IMMATURE_CLASS_WRONG_FIELD_ORDER | Style - Class orders instance fields before static fields | INFO | CODE_SMELL |
| fb-contrib:IOI_COPY_WITH_READER | Performance - Method performs bulk stream copy with a java.io.Reader derived input | MAJOR | BUG |
| fb-contrib:IOI_DOUBLE_BUFFER_COPY | Performance - Method passes a Buffered Stream/Reader/Writer to a already buffering copy method | MAJOR | BUG |
| fb-contrib:IOI_UNENDED_ZLIB_OBJECT | Performance - Method creates a ZLIB Inflater or Deflater and doesn't appear to end() it | MAJOR | BUG |
| fb-contrib:IPU_IMPROPER_PROPERTIES_USE | Correctness - Method puts non-String values into a Properties object | MAJOR | BUG |
| fb-contrib:IPU_IMPROPER_PROPERTIES_USE_SETPROPERTY | Correctness - Method uses Properties.put instead of Properties.setProperty | MAJOR | BUG |
| fb-contrib:ISB_EMPTY_STRING_APPENDING | Performance - Method concatenates an empty string to effect type conversion | MAJOR | BUG |
| fb-contrib:ISB_TOSTRING_APPENDING | Correctness - Method concatenates the result of a toString() call | MAJOR | BUG |
| fb-contrib:ITU_INAPPROPRIATE_TOSTRING_USE | Correctness - Method performs algorithmic operations on the result of a toString() call | MAJOR | BUG |
| fb-contrib:JPAI_HC_EQUALS_ON_MANAGED_ENTITY | Correctness - JPA Entity with Generated @Id defined with hashCode/equals | MAJOR | BUG |
| fb-contrib:JPAI_IGNORED_MERGE_RESULT | Correctness - Method ignores the return value of EntityManager.merge | MAJOR | BUG |
| fb-contrib:JPAI_INEFFICIENT_EAGER_FETCH | Correctness - OneToMany join specifies 1+n EAGER join | MAJOR | BUG |
| fb-contrib:JPAI_NON_PROXIED_TRANSACTION_CALL | Correctness - Method annotated with @Transactional is called from a non Spring proxy | MAJOR | BUG |
| fb-contrib:JPAI_NON_SPECIFIED_TRANSACTION_EXCEPTION_HANDLING | Correctness - Method does not specify how to handle transaction when exception is thrown | MAJOR | BUG |
| fb-contrib:JPAI_TRANSACTION_ON_NON_PUBLIC_METHOD | Correctness - Method has a Spring @Transactional annotation on it, but is non-public | MAJOR | BUG |
| fb-contrib:JPAI_UNNECESSARY_TRANSACTION_EXCEPTION_HANDLING | Correctness - Method declares handling a transactional exception that won't be thrown | MAJOR | BUG |
| fb-contrib:JVR_JDBC_VENDOR_RELIANCE | Correctness - Method uses JDBC vendor specific classes and methods | MAJOR | BUG |
| fb-contrib:JXI_GET_ENDPOINT_CONSUMES_CONTENT | Correctness - JAX-RS Method implements a GET request but consumes input | MAJOR | BUG |
| fb-contrib:JXI_INVALID_CONTEXT_PARAMETER_TYPE | Correctness - JAX-RS Method specifies an invalid @Context parameter type | MAJOR | BUG |
| fb-contrib:JXI_PARM_PARAM_NOT_FOUND_IN_PATH | Correctness - JAX-RS Method specifies non-resolveable @PathParam | MAJOR | BUG |
| fb-contrib:JXI_UNDEFINED_PARAMETER_SOURCE_IN_ENDPOINT | Correctness - JAX-RS Method defines a parameter that has no @*Param or @Context annotation, or @Consumes method annotation | MAJOR | BUG |
| fb-contrib:LEST_LOST_EXCEPTION_STACK_TRACE | Correctness - Method throws alternative exception from catch block without history | MAJOR | BUG |
| fb-contrib:LGO_LINGERING_GRAPHICS_OBJECT | Performance - Method allocations a java.awt.Graphics object without disposing it | MAJOR | BUG |
| fb-contrib:LII_LIST_INDEXED_ITERATING | Style - Method uses integer based for loops to iterate over a List | INFO | CODE_SMELL |
| fb-contrib:LO_APPENDED_STRING_IN_FORMAT_STRING | Performance - Method passes a concatenated string to SLF4J's or Log4j2's format string | MAJOR | BUG |
| fb-contrib:LO_EMBEDDED_SIMPLE_STRING_FORMAT_IN_FORMAT_STRING | Correctness - Method passes a simple String.format result to an SLF4J's or Log4j2's format string | MAJOR | BUG |
| fb-contrib:LO_EXCEPTION_WITH_LOGGER_PARMS | Correctness - Method creates exception with logger parameter markers in message | MAJOR | BUG |
| fb-contrib:LO_INCORRECT_NUMBER_OF_ANCHOR_PARAMETERS | Correctness - Method passes an incorrect number of parameters to an SLF4J or Log4j2 logging statement | MAJOR | BUG |
| fb-contrib:LO_INVALID_FORMATTING_ANCHOR | Correctness - Method attempts to log using numbered formatting anchors | MAJOR | BUG |
| fb-contrib:LO_INVALID_STRING_FORMAT_NOTATION | Correctness - Method attempts to log using String.format notation | MAJOR | BUG |
| fb-contrib:LO_LOGGER_LOST_EXCEPTION_STACK_TRACE | Correctness - Method incorrectly passes exception as first argument to logger method | MAJOR | BUG |
| fb-contrib:LO_NON_PRIVATE_STATIC_LOGGER | Correctness - Class defines non private logger using a static class context | MAJOR | BUG |
| fb-contrib:LO_STUTTERED_MESSAGE | Style - Method stutters exception message in logger | INFO | CODE_SMELL |
| fb-contrib:LO_SUSPECT_LOG_CLASS | Correctness - Method specifies an unrelated class when allocating a Logger | MAJOR | BUG |
| fb-contrib:LO_SUSPECT_LOG_PARAMETER | Correctness - Constructor declares a Logger parameter | MAJOR | BUG |
| fb-contrib:LO_TOSTRING_PARAMETER | Correctness - Method explicitly calls toString() on a logger parameter | MAJOR | BUG |
| fb-contrib:LSYC_LOCAL_SYNCHRONIZED_COLLECTION | Correctness - Method creates local variable-based synchronized collection | MAJOR | BUG |
| fb-contrib:LUI_USE_COLLECTION_ADD | Correctness - Method passes a temporary one item list to Collection.addAll() | MAJOR | BUG |
| fb-contrib:LUI_USE_GET0 | Correctness - Method uses collection streaming to get first item in a List | MAJOR | BUG |
| fb-contrib:LUI_USE_SINGLETON_LIST | Correctness - Method builds a list from one element using Arrays.asList | MAJOR | BUG |
| fb-contrib:LUI_VACUOUS_ADDALL | Correctness - Method passes an empty collection to.addAll() | MAJOR | BUG |
| fb-contrib:MAC_MANUAL_ARRAY_COPY | Performance - Method copies arrays manually | MAJOR | BUG |
| fb-contrib:MDM_BIGDECIMAL_EQUALS | Correctness - Method calls BigDecimal.equals() | MAJOR | BUG |
| fb-contrib:MDM_INETADDRESS_GETLOCALHOST | Correctness - Method calls InetAddress.getLocalHost() | MAJOR | BUG |
| fb-contrib:MDM_LOCK_ISLOCKED | Multi-threading - Method tests if a lock is locked | MAJOR | BUG |
| fb-contrib:MDM_PROMISCUOUS_SERVERSOCKET | Correctness - Method creates promiscuous ServerSocket object | MAJOR | BUG |
| fb-contrib:MDM_RANDOM_SEED | Correctness - Method creates insecure Random object | MAJOR | BUG |
| fb-contrib:MDM_RUNFINALIZATION | Correctness - Method triggers finalization | MAJOR | BUG |
| fb-contrib:MDM_RUNTIME_EXIT_OR_HALT | Correctness - Method calls Runtime.exit() or Runtime.halt() | MAJOR | BUG |
| fb-contrib:MDM_SECURERANDOM | Correctness - Method calls deprecated SecureRandom method | MAJOR | BUG |
| fb-contrib:MDM_SIGNAL_NOT_SIGNALALL | Multi-threading - Method calls Condition.signal() rather than Condition.signalAll() | MAJOR | BUG |
| fb-contrib:MDM_STRING_BYTES_ENCODING | Correctness - Method encodes String bytes without specifying the character encoding | MAJOR | BUG |
| fb-contrib:MDM_THREAD_FAIRNESS | Multi-threading - Method ignores Lock's fairness settings by calling tryLock() | MAJOR | BUG |
| fb-contrib:MDM_THREAD_PRIORITIES | Multi-threading - Method uses suspicious thread priorities | MAJOR | BUG |
| fb-contrib:MDM_THREAD_YIELD | Multi-threading - Method attempts to manually schedule threads | MAJOR | BUG |
| fb-contrib:MDM_WAIT_WITHOUT_TIMEOUT | Multi-threading - Method sleeps without timeout | MAJOR | BUG |
| fb-contrib:MOM_MISLEADING_OVERLOAD_MODEL | Style - Class 'overloads' a method with both instance and static versions | INFO | CODE_SMELL |
| fb-contrib:MUC_MODIFYING_UNMODIFIABLE_COLLECTION | Correctness - This method attempts to modify collection that appears to possibly be immutable | MAJOR | BUG |
| fb-contrib:MUI_CALLING_SIZE_ON_SUBCONTAINER | Correctness - Method calls size() on a sub collection of a Map | MAJOR | BUG |
| fb-contrib:MUI_CONTAINSKEY_BEFORE_GET | Correctness - Method check a map with containsKey(), before using get() | MAJOR | BUG |
| fb-contrib:MUI_GET_BEFORE_REMOVE | Correctness - Method gets an item from a map with get(), before using remove() | MAJOR | BUG |
| fb-contrib:MUI_NULL_CHECK_ON_MAP_SUBSET_ACCESSOR | Correctness - Method checks whether the keySet(), entrySet() or values() collection of a Map is null | MAJOR | BUG |
| fb-contrib:MUI_USE_CONTAINSKEY | Correctness - Method calls keySet() just to call contains, use containsKey instead | MAJOR | BUG |
| fb-contrib:NAB_NEEDLESS_AUTOBOXING_CTOR | Performance - Method passes primitive wrapper to same primitive wrapper constructor | MAJOR | BUG |
| fb-contrib:NAB_NEEDLESS_AUTOBOXING_VALUEOF | Performance - Method passes primitive wrapper to Wrapper class valueOf method | MAJOR | BUG |
| fb-contrib:NAB_NEEDLESS_BOXING_PARSE | Performance - Method converts String to primitive using excessive boxing | MAJOR | BUG |
| fb-contrib:NAB_NEEDLESS_BOXING_STRING_CTOR | Performance - Method passes parsed string to primitive wrapper constructor | MAJOR | BUG |
| fb-contrib:NAB_NEEDLESS_BOX_TO_CAST | Performance - Method creates Boxed primitive from primitive only to cast to another primitive type | MAJOR | BUG |
| fb-contrib:NAB_NEEDLESS_BOX_TO_UNBOX | Performance - Method creates Boxed primitive from primitive only to get primitive value | MAJOR | BUG |
| fb-contrib:NCMU_NON_COLLECTION_METHOD_USE | Style - Method uses old non collections interface methods | INFO | CODE_SMELL |
| fb-contrib:NCS_NEEDLESS_CUSTOM_SERIALIZATION | Correctness - Method needlessly implements what is default streaming behavior | MAJOR | BUG |
| fb-contrib:NFF_NON_FUNCTIONAL_FIELD | Correctness - Serializable class defines a final transient field | MAJOR | BUG |
| fb-contrib:NIR_NEEDLESS_INSTANCE_RETRIEVAL | Performance - Method retrieves instance to load static member | MAJOR | BUG |
| fb-contrib:NMCS_NEEDLESS_MEMBER_COLLECTION_SYNCHRONIZATION | Performance - Class defines unneeded synchronization on member collection | MAJOR | BUG |
| fb-contrib:NOS_NON_OWNED_SYNCHRONIZATION | Style - Class uses non owned variables to synchronize on | INFO | CODE_SMELL |
| fb-contrib:NPMC_NON_PRODUCTIVE_METHOD_CALL | Correctness - Method ignores return value of a non mutating method | MAJOR | BUG |
| fb-contrib:NRTL_NON_RECYCLEABLE_TAG_LIB | Correctness - Tag library is not recycleable | MAJOR | BUG |
| fb-contrib:NSE_NON_SYMMETRIC_EQUALS | Correctness - Equals method compares this object against other types in a non symmetric way | MAJOR | BUG |
| fb-contrib:OCP_OVERLY_CONCRETE_COLLECTION_PARAMETER | Style - Method needlessly defines Collection parameter with concrete classes | INFO | CODE_SMELL |
| fb-contrib:OC_OVERZEALOUS_CASTING | Correctness - Method manually casts the right hand side of an assignment more specifically than needed | MAJOR | BUG |
| fb-contrib:ODN_ORPHANED_DOM_NODE | Correctness - Method creates DOM node but doesn't attach it to a document | MAJOR | BUG |
| fb-contrib:OI_OPTIONAL_ISSUES_CHECKING_REFERENCE | Correctness - Method checks an Optional reference for null | MAJOR | BUG |
| fb-contrib:OI_OPTIONAL_ISSUES_ISPRESENT_PREFERRED | Correctness - Method uses Optional.equals(Optional.empty()), when Optional.isPresent is more readable | MAJOR | BUG |
| fb-contrib:OI_OPTIONAL_ISSUES_PRIMITIVE_VARIANT_PREFERRED | Correctness - Method uses a java.util.Optional when use of OptionalInt, OptionalLong, OptionalDouble would be more clear | MAJOR | BUG |
| fb-contrib:OI_OPTIONAL_ISSUES_USES_DELAYED_EXECUTION | Correctness - Method uses delayed execution of a block of code that is trivial | MAJOR | BUG |
| fb-contrib:OI_OPTIONAL_ISSUES_USES_IMMEDIATE_EXECUTION | Correctness - Method uses immediate execution of a block of code that is often not used | MAJOR | BUG |
| fb-contrib:OI_OPTIONAL_ISSUES_USES_ORELSEGET_WITH_NULL | Correctness - Method uses Optional.orElseGet(null) | MAJOR | BUG |
| fb-contrib:OPM_OVERLY_PERMISSIVE_METHOD | Style - This method is declared more permissively than is used in the code base | INFO | CODE_SMELL |
| fb-contrib:PCAIL_POSSIBLE_CONSTANT_ALLOCATION_IN_LOOP | Performance - Method allocates an object that is used in a constant way in a loop | MAJOR | BUG |
| fb-contrib:PCOA_PARTIALLY_CONSTRUCTED_OBJECT_ACCESS | Correctness - Constructor makes call to non-final method | MAJOR | BUG |
| fb-contrib:PDP_POORLY_DEFINED_PARAMETER | Correctness - Method defines parameters more abstractly than needed to function properly | MAJOR | BUG |
| fb-contrib:PIS_POSSIBLE_INCOMPLETE_SERIALIZATION | Correctness - Class doesn't serialize superclass fields | MAJOR | BUG |
| fb-contrib:PKI_SUPERFLUOUS_ROUTE_SPECIFICATION | Correctness - Method specifies superfluous routes thru route() or concat() | MAJOR | BUG |
| fb-contrib:PMB_INSTANCE_BASED_THREAD_LOCAL | Correctness - Field is an instance based ThreadLocal variable | MAJOR | BUG |
| fb-contrib:PMB_LOCAL_BASED_JAXB_CONTEXT | Correctness - Local JAXBContext created on demand | MAJOR | BUG |
| fb-contrib:PMB_POSSIBLE_MEMORY_BLOAT | Correctness - Potential memory bloat in static field | MAJOR | BUG |
| fb-contrib:PSC_SUBOPTIMAL_COLLECTION_SIZING | Performance - Method uses suboptimal sizing to allocate a collection | MAJOR | BUG |
| fb-contrib:PUS_POSSIBLE_UNSUSPECTED_SERIALIZATION | Correctness - Method serializes an instance of a non-static inner class | MAJOR | BUG |
| fb-contrib:ROOM_REFLECTION_ON_OBJECT_METHODS | Correctness - Method uses reflection to call a method available on java.lang.Object | MAJOR | BUG |
| fb-contrib:S508C_APPENDED_STRING | Correctness - Method passes appended string to title/label of component | MAJOR | BUG |
| fb-contrib:S508C_NON_ACCESSIBLE_JCOMPONENT | Correctness - Class extends JComponent but does not implement Accessible interface | MAJOR | BUG |
| fb-contrib:S508C_NON_TRANSLATABLE_STRING | Correctness - Method passes constant string to title/label of component | MAJOR | BUG |
| fb-contrib:S508C_NO_SETLABELFOR | Correctness - JLabel doesn't specify what it's labeling | MAJOR | BUG |
| fb-contrib:S508C_NO_SETSIZE | Correctness - Window sets size manually, and doesn't use pack | MAJOR | BUG |
| fb-contrib:S508C_NULL_LAYOUT | Correctness - GUI uses absolute layout | MAJOR | BUG |
| fb-contrib:S508C_SET_COMP_COLOR | Correctness - Method explicitly sets the color of a Component | MAJOR | BUG |
| fb-contrib:SAT_SUSPICIOUS_ARGUMENT_TYPES | Correctness - This method invokes a method with parameters that seem incorrect for their intended use | MAJOR | BUG |
| fb-contrib:SCA_SUSPICIOUS_CLONE_ALGORITHM | Correctness - Clone method stores a new value to member field of source object | MAJOR | BUG |
| fb-contrib:SCII_SPOILED_CHILD_INTERFACE_IMPLEMENTOR | Style - Class implements interface by relying on unknowing superclass methods | INFO | CODE_SMELL |
| fb-contrib:SCI_SYNCHRONIZED_COLLECTION_ITERATORS | Correctness - Method creates iterators on synchronized collections | MAJOR | BUG |
| fb-contrib:SCRV_SUSPICIOUS_COMPARATOR_RETURN_VALUES | Correctness - Comparator method doesn't seem to return all ordering values | MAJOR | BUG |
| fb-contrib:SCR_SLOPPY_CLASS_REFLECTION | Style - Method accesses statically bound class with Class.forName | INFO | CODE_SMELL |
| fb-contrib:SCSS_SUSPICIOUS_CLUSTERED_SESSION_SUPPORT | Correctness - Method modifies an http session attribute without calling setAttribute | MAJOR | BUG |
| fb-contrib:SEC_SIDE_EFFECT_CONSTRUCTOR | Style - Method uses a Side Effect Constructor | INFO | CODE_SMELL |
| fb-contrib:SGSU_SUSPICIOUS_GETTER_SETTER_USE | Correctness - Method uses same bean's getter value for setter | MAJOR | BUG |
| fb-contrib:SG_SLUGGISH_GUI | Performance - Method performs time consuming operation in GUI thread | MAJOR | BUG |
| fb-contrib:SIL_SQL_IN_LOOP | Performance - Method executes SQL queries inside of loops | MAJOR | BUG |
| fb-contrib:SJVU_SUSPICIOUS_JDK_VERSION_USE | Correctness - Method uses rt.jar class or method that does not exist | MAJOR | BUG |
| fb-contrib:SLS_SUSPICIOUS_LOOP_SEARCH | Correctness - This method continues a loop after finding an equality condition | MAJOR | BUG |
| fb-contrib:SMII_STATIC_METHOD_INSTANCE_INVOCATION | Style - Method calls static method on instance reference | INFO | CODE_SMELL |
| fb-contrib:SNG_SUSPICIOUS_NULL_FIELD_GUARD | Correctness - Method tests a field for not null as guard and reassigns it | MAJOR | BUG |
| fb-contrib:SNG_SUSPICIOUS_NULL_LOCAL_GUARD | Correctness - Method tests a local variable for not null as guard and reassigns it | MAJOR | BUG |
| fb-contrib:SPP_CONVERSION_OF_STRING_LITERAL | Correctness - Method converts a String literal | MAJOR | BUG |
| fb-contrib:SPP_DOUBLE_APPENDED_LITERALS | Performance - Method appends two literal strings back to back to a StringBuilder | MAJOR | BUG |
| fb-contrib:SPP_EMPTY_CASING | Style - Method passes an empty string to equalsIgnoreCase or compareToIgnoreCase | INFO | CODE_SMELL |
| fb-contrib:SPP_EQUALS_ON_STRING_BUILDER | Correctness - Method calls equals(Object o) on a StringBuilder or StringBuffer | MAJOR | BUG |
| fb-contrib:SPP_INTERN_ON_CONSTANT | Correctness - Method calls intern on a string constant | MAJOR | BUG |
| fb-contrib:SPP_INVALID_BOOLEAN_NULL_CHECK | Correctness - Method uses invalid C++ style null check on Boolean | MAJOR | BUG |
| fb-contrib:SPP_INVALID_CALENDAR_COMPARE | Correctness - Method passes a non calendar object to Calendar.before or Calendar.after | MAJOR | BUG |
| fb-contrib:SPP_NEGATIVE_BITSET_ITEM | Correctness - Method passes a negative number as a bit to a BitSet which isn't supported | MAJOR | BUG |
| fb-contrib:SPP_NON_ARRAY_PARM | Correctness - Method passes a non array object to a parameter that expects an array | MAJOR | BUG |
| fb-contrib:SPP_NON_USEFUL_TOSTRING | Style - Method calls toString() on an instance of a class that hasn't overridden toString() | INFO | CODE_SMELL |
| fb-contrib:SPP_NO_CHAR_SB_CTOR | Correctness - Method appears to pass character to StringBuffer or StringBuilder integer constructor | MAJOR | BUG |
| fb-contrib:SPP_NULL_BEFORE_INSTANCEOF | Correctness - Method checks a reference for null before calling instanceof | MAJOR | BUG |
| fb-contrib:SPP_PASSING_THIS_AS_PARM | Correctness - Method call passes object that the method is called on as a parameter | MAJOR | BUG |
| fb-contrib:SPP_SERIALVER_SHOULD_BE_PRIVATE | Style - Class defines a serialVersionUID as non private | INFO | CODE_SMELL |
| fb-contrib:SPP_STATIC_FORMAT_STRING | Correctness - Method calls String.format on a static (non parameterized) format string | MAJOR | BUG |
| fb-contrib:SPP_STRINGBUFFER_WITH_EMPTY_STRING | Performance - Method passes an empty string to StringBuffer of StringBuilder constructor | MAJOR | BUG |
| fb-contrib:SPP_STRINGBUILDER_IS_MUTABLE | Correctness - Method needlessly assigns a StringBuilder to itself, as it's mutable | MAJOR | BUG |
| fb-contrib:SPP_STUTTERED_ASSIGNMENT | Correctness - Method assigns a value to a local twice in a row | MAJOR | BUG |
| fb-contrib:SPP_SUSPECT_STRING_TEST | Correctness - Method possibly mixes up normal strings and empty strings in branching logic | MAJOR | BUG |
| fb-contrib:SPP_TEMPORARY_TRIM | Style - Method trims a String temporarily | INFO | CODE_SMELL |
| fb-contrib:SPP_USELESS_CASING | Performance - Method compares string without case after enforcing a case | MAJOR | BUG |
| fb-contrib:SPP_USE_CHARAT | Performance - Method fetches character array just to do the equivalent of the charAt method | MAJOR | BUG |
| fb-contrib:SPP_USE_GET0 | Performance - Method uses iterator().next() on a List to get the first item | MAJOR | BUG |
| fb-contrib:SPP_USE_GETPROPERTY | Style - Method calls getProperties just to get one property, use getProperty instead | INFO | CODE_SMELL |
| fb-contrib:SPP_USE_ISEMPTY | Style - Method checks the size of a collection against zero rather than using isEmpty() | INFO | CODE_SMELL |
| fb-contrib:SPP_USE_ISNAN | Correctness - Method incorrectly compares a floating point number to NaN | MAJOR | BUG |
| fb-contrib:SPP_USE_MATH_CONSTANT | Correctness - Method uses non-standard math constant | MAJOR | BUG |
| fb-contrib:SPP_USE_STRINGBUILDER_LENGTH | Performance - Method converts StringBuffer or Builder to String just to get its length | MAJOR | BUG |
| fb-contrib:SPP_USE_ZERO_WITH_COMPARATOR | Correctness - Method compares the result of a compareTo method to a value other than zero | MAJOR | BUG |
| fb-contrib:SPP_WRONG_COMMONS_TO_STRING_OBJECT | Correctness - Method does not pass an object to commons-lang's ToStringBuilder | MAJOR | BUG |
| fb-contrib:SSCU_SUSPICIOUS_SHADED_CLASS_USE | Correctness - Method calls a method from a class that has been shaded by a 3rdparty jar | MAJOR | BUG |
| fb-contrib:STB_STACKED_TRY_BLOCKS | Style - Method stacks similar try/catch blocks | INFO | CODE_SMELL |
| fb-contrib:STS_SPURIOUS_THREAD_STATES | Multi-threading - Method calls wait, notify or notifyAll on a Thread instance | MAJOR | BUG |
| fb-contrib:STT_TOSTRING_MAP_KEYING | Style - This method uses a concatenated String as a map key | INFO | CODE_SMELL |
| fb-contrib:SUI_CONTAINS_BEFORE_ADD | Correctness - Method checks for an item in a set with contains, before using add() | MAJOR | BUG |
| fb-contrib:SUI_CONTAINS_BEFORE_REMOVE | Correctness - Method checks for an item in a set with contains, before using remove() | MAJOR | BUG |
| fb-contrib:SWCO_SUSPICIOUS_WAIT_ON_CONCURRENT_OBJECT | Correctness - Method calls wait when await was probably intended | MAJOR | BUG |
| fb-contrib:TR_TAIL_RECURSION | Performance - Method employs tail recursion | MAJOR | BUG |
| fb-contrib:UAA_USE_ADD_ALL | Style - Method uses simple loop to copy contents of one collection to another | INFO | CODE_SMELL |
| fb-contrib:UAC_UNNECESSARY_API_CONVERSION_DATE_TO_INSTANT | Correctness - Method constructs a Date object, merely to convert it to an Instant object | MAJOR | BUG |
| fb-contrib:UAC_UNNECESSARY_API_CONVERSION_FILE_TO_PATH | Correctness - Method constructs a File object, merely to convert it to a Path object | MAJOR | BUG |
| fb-contrib:UCC_UNRELATED_COLLECTION_CONTENTS | Style - Method adds unrelated types to collection or array | INFO | CODE_SMELL |
| fb-contrib:UEC_USE_ENUM_COLLECTIONS | Performance - Class uses an ordinary set or map with an enum class as the key | MAJOR | BUG |
| fb-contrib:UJM_UNJITABLE_METHOD | Performance - This method is too long to be compiled by the JIT | MAJOR | BUG |
| fb-contrib:UMTP_UNBOUND_METHOD_TEMPLATE_PARAMETER | Correctness - Method declares unbound method template parameter(s) | MAJOR | BUG |
| fb-contrib:UNNC_UNNECESSARY_NEW_NULL_CHECK | Correctness - Method checks the result of a new allocation | MAJOR | BUG |
| fb-contrib:URV_CHANGE_RETURN_TYPE | Style - Method returns more specific type of object than declared | INFO | CODE_SMELL |
| fb-contrib:URV_INHERITED_METHOD_WITH_RELATED_TYPES | Style - Inherited method returns more specific type of object than declared | INFO | CODE_SMELL |
| fb-contrib:URV_UNRELATED_RETURN_VALUES | Style - Method returns different types of unrelated Objects | INFO | CODE_SMELL |
| fb-contrib:USFW_UNSYNCHRONIZED_SINGLETON_FIELD_WRITES | Correctness - Method of Singleton class writes to a field in an unsynchronized manner | MAJOR | BUG |
| fb-contrib:USS_USE_STRING_SPLIT | Style - Method builds String array using String Tokenizing | INFO | CODE_SMELL |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_ACTUAL_CONSTANT | Style - JUnit test method passes constant to second (actual) assertion parameter | INFO | CODE_SMELL |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_ASSERT_USED | Correctness - JUnit test method uses Java asserts rather than a JUnit assertion | MAJOR | BUG |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_BOOLEAN_ASSERT | Style - JUnit test method asserts that a value is equal to true or false | INFO | CODE_SMELL |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_IMPOSSIBLE_NULL | Correctness - JUnit test method asserts that an autoboxed value is not null | CRITICAL | BUG |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_INEXACT_DOUBLE | Style - JUnit test method asserts that two doubles are exactly equal | INFO | CODE_SMELL |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_NO_ASSERT | Correctness - JUnit test method appears to have no assertions | MAJOR | BUG |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_USE_ASSERT_EQUALS | Correctness - JUnit test method passes boolean expression to Assert.assertFalse / Assert.assertTrue | MAJOR | BUG |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_USE_ASSERT_NOT_EQUALS | Correctness - JUnit test method passes boolean expression to Assert.assertFalse / Assert.assertTrue | MAJOR | BUG |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_USE_ASSERT_NOT_NULL | Correctness - JUnit test method passes null Assert.assertNotEquals | MAJOR | BUG |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_USE_ASSERT_NULL | Correctness - JUnit test method passes null Assert.assertEquals | MAJOR | BUG |
| fb-contrib:UTAO_JUNIT_ASSERTION_ODDITIES_USING_DEPRECATED | Correctness - JUnit 4 test using deprecated junit.framework.* classes | MAJOR | BUG |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_ACTUAL_CONSTANT | Style - TestNG test method passes constant to first (actual) assertion parameter | INFO | CODE_SMELL |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_ASSERT_USED | Correctness - TestNG test method uses Java asserts rather than a TestNG assertion | MAJOR | BUG |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_BOOLEAN_ASSERT | Style - TestNG test method asserts that a value is true or false | INFO | CODE_SMELL |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_IMPOSSIBLE_NULL | Correctness - TestNG test method asserts that an autoboxed value is not null | CRITICAL | BUG |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_INEXACT_DOUBLE | Style - TestNG test method asserts that two doubles are exactly equal | INFO | CODE_SMELL |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_NO_ASSERT | Correctness - TestNG test method appears to have no assertions | MAJOR | BUG |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_USE_ASSERT_EQUALS | Correctness - TestNG test method passes boolean expression to Assert.assertFalse / Assert.assertTrue | MAJOR | BUG |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_USE_ASSERT_NOT_EQUALS | Correctness - TestNG test method passes boolean expression to Assert.assertFalse / Assert.assertTrue | MAJOR | BUG |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_USE_ASSERT_NOT_NULL | Correctness - TestNG test method passes null Assert.assertNotEquals | MAJOR | BUG |
| fb-contrib:UTAO_TESTNG_ASSERTION_ODDITIES_USE_ASSERT_NULL | Correctness - TestNG test method passes null Assert.assertEquals | MAJOR | BUG |
| fb-contrib:UTA_USE_TO_ARRAY | Style - Method manually creates array from collection | INFO | CODE_SMELL |
| fb-contrib:UTWR_USE_TRY_WITH_RESOURCES | Style - Method manually handles closing an auto-closeable resource | INFO | CODE_SMELL |
| fb-contrib:UVA_REMOVE_NULL_ARG | Style - Method passes explicit null value to var arg parameter | INFO | CODE_SMELL |
| fb-contrib:UVA_USE_VAR_ARGS | Style - Method defines parameter list with array as last argument, rather than vararg | INFO | CODE_SMELL |
| fb-contrib:WEM_OBSCURING_EXCEPTION | Style - Method throws a java.lang.Exception that wraps a more useful exception | INFO | CODE_SMELL |
| fb-contrib:WEM_WEAK_EXCEPTION_MESSAGING | Style - Method throws exception with static message string | INFO | CODE_SMELL |
| fb-contrib:WI_DUPLICATE_WIRED_TYPES | Correctness - Class auto wires the same object into two separate fields in a class hierarchy | MAJOR | BUG |
| fb-contrib:WI_MANUALLY_ALLOCATING_AN_AUTOWIRED_BEAN | Correctness - Method allocates an object with new when the class is defined as an autowireable bean | MAJOR | BUG |
| fb-contrib:WI_WIRING_OF_STATIC_FIELD | Correctness - Static field is autowired | MAJOR | BUG |
| fb-contrib:WOC_WRITE_ONLY_COLLECTION_LOCAL | Correctness - Method creates and initializes a collection but never reads or gains information from it | MAJOR | BUG |
| findbugs:AA_ASSERTION_OF_ARGUMENTS | Bad practice - Assertion is used to validate an argument of a public method | MAJOR | CODE_SMELL |
| findbugs:AM_CREATES_EMPTY_JAR_FILE_ENTRY | Bad practice - Creates an empty jar file entry | MAJOR | CODE_SMELL |
| findbugs:AM_CREATES_EMPTY_ZIP_FILE_ENTRY | Bad practice - Creates an empty zip file entry | MAJOR | CODE_SMELL |
| findbugs:ASE_ASSERTION_WITH_SIDE_EFFECT | Security - Expression in assertion may produce a side effect | MAJOR | VULNERABILITY |
| findbugs:ASE_ASSERTION_WITH_SIDE_EFFECT_METHOD | Security - Method invoked in assertion may produce a side effect | MAJOR | VULNERABILITY |
| findbugs:AT_NONATOMIC_64BIT_PRIMITIVE | Multi-threading - This write of this 64-bit primitive variable may not atomic | MAJOR | BUG |
| findbugs:AT_NONATOMIC_OPERATIONS_ON_SHARED_VARIABLE | Multi-threading - Operation on shared variable is not atomic | MAJOR | BUG |
| findbugs:AT_OPERATION_SEQUENCE_ON_CONCURRENT_ABSTRACTION | Multi-threading - Sequence of calls to concurrent abstraction may not be atomic | MAJOR | BUG |
| findbugs:AT_STALE_THREAD_WRITE_OF_PRIMITIVE | Multi-threading - This write of this shared primitive variable may not be visible to other threads | MAJOR | BUG |
| findbugs:AT_UNSAFE_RESOURCE_ACCESS_IN_THREAD | Multi-threading - Operation on resource is not safe in a multithreaded context | MAJOR | BUG |
| findbugs:BAC_BAD_APPLET_CONSTRUCTOR | Correctness - Bad Applet Constructor relies on uninitialized AppletStub | MAJOR | BUG |
| findbugs:BC_BAD_CAST_TO_ABSTRACT_COLLECTION | Style - Questionable cast to abstract collection | INFO | CODE_SMELL |
| findbugs:BC_BAD_CAST_TO_CONCRETE_COLLECTION | Style - Questionable cast to concrete collection | INFO | CODE_SMELL |
| findbugs:BC_EQUALS_METHOD_SHOULD_WORK_FOR_ALL_OBJECTS | Bad practice - Equals method should not assume anything about the type of its argument | MAJOR | CODE_SMELL |
| findbugs:BC_IMPOSSIBLE_CAST | Correctness - Impossible cast | CRITICAL | BUG |
| findbugs:BC_IMPOSSIBLE_CAST_PRIMITIVE_ARRAY | Correctness - Impossible cast involving primitive array | CRITICAL | BUG |
| findbugs:BC_IMPOSSIBLE_DOWNCAST | Correctness - Impossible downcast | CRITICAL | BUG |
| findbugs:BC_IMPOSSIBLE_DOWNCAST_OF_TOARRAY | Correctness - Impossible downcast of toArray() result | CRITICAL | BUG |
| findbugs:BC_IMPOSSIBLE_INSTANCEOF | Correctness - instanceof will always return false | CRITICAL | BUG |
| findbugs:BC_UNCONFIRMED_CAST | Style - Unchecked/unconfirmed cast | INFO | CODE_SMELL |
| findbugs:BC_UNCONFIRMED_CAST_OF_RETURN_VALUE | Style - Unchecked/unconfirmed cast of return value from method | INFO | CODE_SMELL |
| findbugs:BC_VACUOUS_INSTANCEOF | Style - instanceof will always return true | INFO | CODE_SMELL |
| findbugs:BIT_ADD_OF_SIGNED_BYTE | Correctness - Bitwise add of signed byte value | MAJOR | BUG |
| findbugs:BIT_AND | Correctness - Incompatible bit masks | MAJOR | BUG |
| findbugs:BIT_AND_ZZ | Correctness - Check to see if ((...) & 0) == 0 | MAJOR | BUG |
| findbugs:BIT_IOR | Correctness - Incompatible bit masks | MAJOR | BUG |
| findbugs:BIT_IOR_OF_SIGNED_BYTE | Correctness - Bitwise OR of signed byte value | MAJOR | BUG |
| findbugs:BIT_SIGNED_CHECK | Bad practice - Check for sign of bitwise operation | MAJOR | CODE_SMELL |
| findbugs:BIT_SIGNED_CHECK_HIGH_BIT | Correctness - Check for sign of bitwise operation involving negative number | MAJOR | BUG |
| findbugs:BOA_BADLY_OVERRIDDEN_ADAPTER | Correctness - Class overrides a method implemented in super class Adapter wrongly | MAJOR | BUG |
| findbugs:BSHIFT_WRONG_ADD_PRIORITY | Correctness - Possible bad parsing of shift operation | MAJOR | BUG |
| findbugs:BX_BOXING_IMMEDIATELY_UNBOXED | Performance - Primitive value is boxed and then immediately unboxed | MAJOR | BUG |
| findbugs:BX_BOXING_IMMEDIATELY_UNBOXED_TO_PERFORM_COERCION | Performance - Primitive value is boxed then unboxed to perform primitive coercion | MAJOR | BUG |
| findbugs:BX_UNBOXED_AND_COERCED_FOR_TERNARY_OPERATOR | Performance - Primitive value is unboxed and coerced for ternary operator | MAJOR | BUG |
| findbugs:BX_UNBOXING_IMMEDIATELY_REBOXED | Performance - Boxed value is unboxed and then immediately reboxed | MAJOR | BUG |
| findbugs:CAA_COVARIANT_ARRAY_ELEMENT_STORE | Correctness - Possibly incompatible element is stored in covariant array | MAJOR | BUG |
| findbugs:CAA_COVARIANT_ARRAY_FIELD | Style - Covariant array assignment to a field | INFO | CODE_SMELL |
| findbugs:CAA_COVARIANT_ARRAY_LOCAL | Style - Covariant array assignment to a local variable | INFO | CODE_SMELL |
| findbugs:CAA_COVARIANT_ARRAY_RETURN | Style - Covariant array is returned from the method | INFO | CODE_SMELL |
| findbugs:CD_CIRCULAR_DEPENDENCY | Style - Test for circular dependencies among classes | INFO | CODE_SMELL |
| findbugs:CI_CONFUSED_INHERITANCE | Style - Class is final but declares protected field | INFO | CODE_SMELL |
| findbugs:CNT_ROUGH_CONSTANT_VALUE | Bad practice - Rough value of known constant found | MAJOR | CODE_SMELL |
| findbugs:CN_IDIOM | Bad practice - Class implements Cloneable but does not define or use clone method | MAJOR | CODE_SMELL |
| findbugs:CN_IDIOM_NO_SUPER_CALL | Bad practice - clone method does not call super.clone() | MAJOR | CODE_SMELL |
| findbugs:CN_IMPLEMENTS_CLONE_BUT_NOT_CLONEABLE | Bad practice - Class defines clone() but doesn't implement Cloneable | MAJOR | CODE_SMELL |
| findbugs:CO_ABSTRACT_SELF | Bad practice - Abstract class defines covariant compareTo() method | MAJOR | CODE_SMELL |
| findbugs:CO_COMPARETO_INCORRECT_FLOATING | Bad practice - compareTo()/compare() incorrectly handles float or double value | MAJOR | CODE_SMELL |
| findbugs:CO_COMPARETO_RESULTS_MIN_VALUE | Bad practice - compareTo()/compare() returns Integer.MIN_VALUE | MAJOR | CODE_SMELL |
| findbugs:CO_SELF_NO_OBJECT | Bad practice - Covariant compareTo() method defined | MAJOR | CODE_SMELL |
| findbugs:CT_CONSTRUCTOR_THROW | Bad practice - Be wary of letting constructors throw exceptions. | MAJOR | CODE_SMELL |
| findbugs:DB_DUPLICATE_BRANCHES | Style - Method uses the same code for two branches | INFO | CODE_SMELL |
| findbugs:DB_DUPLICATE_SWITCH_CLAUSES | Style - Method uses the same code for two switch clauses | INFO | CODE_SMELL |
| findbugs:DCN_NULLPOINTER_EXCEPTION | Style - NullPointerException caught | INFO | CODE_SMELL |
| findbugs:DC_DOUBLECHECK | Multi-threading - Possible double-check of field | MAJOR | BUG |
| findbugs:DC_PARTIALLY_CONSTRUCTED | Multi-threading - Possible exposure of partially initialized object | MAJOR | BUG |
| findbugs:DE_MIGHT_DROP | Bad practice - Method might drop exception | MAJOR | CODE_SMELL |
| findbugs:DE_MIGHT_IGNORE | Bad practice - Method might ignore exception | MAJOR | CODE_SMELL |
| findbugs:DLS_DEAD_LOCAL_INCREMENT_IN_RETURN | Correctness - Useless increment in return statement | MAJOR | BUG |
| findbugs:DLS_DEAD_LOCAL_STORE | Style - Dead store to local variable | INFO | CODE_SMELL |
| findbugs:DLS_DEAD_LOCAL_STORE_IN_RETURN | Style - Useless assignment in return statement | INFO | CODE_SMELL |
| findbugs:DLS_DEAD_LOCAL_STORE_OF_NULL | Style - Dead store of null to local variable | INFO | CODE_SMELL |
| findbugs:DLS_DEAD_LOCAL_STORE_SHADOWS_FIELD | Style - Dead store to local variable that shadows field | INFO | CODE_SMELL |
| findbugs:DLS_DEAD_STORE_OF_CLASS_LITERAL | Correctness - Dead store of class literal | MAJOR | BUG |
| findbugs:DLS_OVERWRITTEN_INCREMENT | Correctness - Overwritten increment | MAJOR | BUG |
| findbugs:DL_SYNCHRONIZATION_ON_BOOLEAN | Multi-threading - Synchronization on Boolean | MAJOR | BUG |
| findbugs:DL_SYNCHRONIZATION_ON_BOXED_PRIMITIVE | Multi-threading - Synchronization on boxed primitive | MAJOR | BUG |
| findbugs:DL_SYNCHRONIZATION_ON_INTERNED_STRING | Multi-threading - Synchronization on interned String | MAJOR | BUG |
| findbugs:DL_SYNCHRONIZATION_ON_SHARED_CONSTANT | Multi-threading - Synchronization on String literal | MAJOR | BUG |
| findbugs:DL_SYNCHRONIZATION_ON_UNSHARED_BOXED_PRIMITIVE | Multi-threading - Synchronization on boxed primitive values | MAJOR | BUG |
| findbugs:DMI_ANNOTATION_IS_NOT_VISIBLE_TO_REFLECTION | Correctness - Cannot use reflection to check for presence of annotation without runtime retention | MAJOR | BUG |
| findbugs:DMI_ARGUMENTS_WRONG_ORDER | Correctness - Reversed method arguments | MAJOR | BUG |
| findbugs:DMI_BAD_MONTH | Correctness - Bad constant value for month | MAJOR | BUG |
| findbugs:DMI_BIGDECIMAL_CONSTRUCTED_FROM_DOUBLE | Correctness - BigDecimal constructed from double that isn't represented precisely | MAJOR | BUG |
| findbugs:DMI_BLOCKING_METHODS_ON_URL | Performance - The equals and hashCode methods of URL are blocking | MAJOR | BUG |
| findbugs:DMI_CALLING_NEXT_FROM_HASNEXT | Correctness - hasNext method invokes next | MAJOR | BUG |
| findbugs:DMI_COLLECTIONS_SHOULD_NOT_CONTAIN_THEMSELVES | Correctness - Collections should not contain themselves | MAJOR | BUG |
| findbugs:DMI_COLLECTION_OF_URLS | Performance - Maps and sets of URLs can be performance hogs | MAJOR | BUG |
| findbugs:DMI_CONSTANT_DB_PASSWORD | Security - Hardcoded constant database password | MAJOR | VULNERABILITY |
| findbugs:DMI_DOH | Correctness - D'oh! A nonsensical method invocation | MAJOR | BUG |
| findbugs:DMI_EMPTY_DB_PASSWORD | Security - Empty database password | MAJOR | VULNERABILITY |
| findbugs:DMI_ENTRY_SETS_MAY_REUSE_ENTRY_OBJECTS | Bad practice - Adding elements of an entry set may fail due to reuse of Entry objects | MAJOR | CODE_SMELL |
| findbugs:DMI_FUTILE_ATTEMPT_TO_CHANGE_MAXPOOL_SIZE_OF_SCHEDULED_THREAD_POOL_EXECUTOR | Correctness - Futile attempt to change max pool size of ScheduledThreadPoolExecutor | MAJOR | BUG |
| findbugs:DMI_HARDCODED_ABSOLUTE_FILENAME | Style - Code contains a hard coded reference to an absolute pathname | INFO | CODE_SMELL |
| findbugs:DMI_INVOKING_HASHCODE_ON_ARRAY | Correctness - Invocation of hashCode on an array | MAJOR | BUG |
| findbugs:DMI_INVOKING_TOSTRING_ON_ANONYMOUS_ARRAY | Correctness - Invocation of toString on an unnamed array | MAJOR | BUG |
| findbugs:DMI_INVOKING_TOSTRING_ON_ARRAY | Correctness - Invocation of toString on an array | MAJOR | BUG |
| findbugs:DMI_LONG_BITS_TO_DOUBLE_INVOKED_ON_INT | Correctness - Double.longBitsToDouble invoked on an int | MAJOR | BUG |
| findbugs:DMI_NONSERIALIZABLE_OBJECT_WRITTEN | Style - Non serializable object written to ObjectOutput | INFO | CODE_SMELL |
| findbugs:DMI_RANDOM_USED_ONLY_ONCE | Bad practice - Random object created and used only once | MAJOR | CODE_SMELL |
| findbugs:DMI_SCHEDULED_THREAD_POOL_EXECUTOR_WITH_ZERO_CORE_THREADS | Correctness - Creation of ScheduledThreadPoolExecutor with zero core threads | MAJOR | BUG |
| findbugs:DMI_THREAD_PASSED_WHERE_RUNNABLE_EXPECTED | Style - Thread passed where Runnable expected | INFO | CODE_SMELL |
| findbugs:DMI_UNSUPPORTED_METHOD | Style - Call to unsupported method | INFO | CODE_SMELL |
| findbugs:DMI_USELESS_SUBSTRING | Style - Invocation of substring(0), which returns the original value | INFO | CODE_SMELL |
| findbugs:DMI_USING_REMOVEALL_TO_CLEAR_COLLECTION | Bad practice - Don't use removeAll to clear a collection | MAJOR | CODE_SMELL |
| findbugs:DMI_VACUOUS_CALL_TO_EASYMOCK_METHOD | Correctness - Useless/vacuous call to EasyMock method | MAJOR | BUG |
| findbugs:DMI_VACUOUS_SELF_COLLECTION_CALL | Correctness - Vacuous call to collections | MAJOR | BUG |
| findbugs:DM_BOOLEAN_CTOR | Performance - Method invokes inefficient Boolean constructor; use Boolean.valueOf(...) instead | MAJOR | BUG |
| findbugs:DM_BOXED_PRIMITIVE_FOR_COMPARE | Performance - Boxing a primitive to compare | MAJOR | BUG |
| findbugs:DM_BOXED_PRIMITIVE_FOR_PARSING | Performance - Boxing/unboxing to parse a primitive | MAJOR | BUG |
| findbugs:DM_BOXED_PRIMITIVE_TOSTRING | Performance - Method allocates a boxed primitive just to call toString | MAJOR | BUG |
| findbugs:DM_CONVERT_CASE | I18n - Consider using Locale parameterized version of invoked method | INFO | CODE_SMELL |
| findbugs:DM_EXIT | Bad practice - Method invokes System.exit(...) | MAJOR | CODE_SMELL |
| findbugs:DM_FP_NUMBER_CTOR | Performance - Method invokes inefficient floating-point Number constructor; use static valueOf instead | MAJOR | BUG |
| findbugs:DM_GC | Performance - Explicit garbage collection; extremely dubious except in benchmarking code | MAJOR | BUG |
| findbugs:DM_INVALID_MIN_MAX | Correctness - Incorrect combination of Math.max and Math.min | MAJOR | BUG |
| findbugs:DM_MONITOR_WAIT_ON_CONDITION | Multi-threading - Monitor wait() called on Condition | MAJOR | BUG |
| findbugs:DM_NEW_FOR_GETCLASS | Performance - Method allocates an object, only to get the class object | MAJOR | BUG |
| findbugs:DM_NEXTINT_VIA_NEXTDOUBLE | Performance - Use the nextInt method of Random rather than nextDouble to generate a random integer | MAJOR | BUG |
| findbugs:DM_NUMBER_CTOR | Performance - Method invokes inefficient Number constructor; use static valueOf instead | MAJOR | BUG |
| findbugs:DM_RUN_FINALIZERS_ON_EXIT | Bad practice - Method invokes dangerous method runFinalizersOnExit | MAJOR | CODE_SMELL |
| findbugs:DM_STRING_CTOR | Performance - Method invokes inefficient new String(String) constructor | MAJOR | BUG |
| findbugs:DM_STRING_TOSTRING | Performance - Method invokes toString() method on a String | MAJOR | BUG |
| findbugs:DM_STRING_VOID_CTOR | Performance - Method invokes inefficient new String() constructor | MAJOR | BUG |
| findbugs:DM_USELESS_THREAD | Multi-threading - A thread was created using the default empty run method | MAJOR | BUG |
| findbugs:DP_CREATE_CLASSLOADER_INSIDE_DO_PRIVILEGED | Malicious code - Classloaders should only be created inside doPrivileged block | INFO | CODE_SMELL |
| findbugs:DP_DO_INSIDE_DO_PRIVILEDGED | Experimental - Method invoked that should be only be invoked inside a doPrivileged block | INFO | CODE_SMELL |
| findbugs:DP_DO_INSIDE_DO_PRIVILEGED | Malicious code - Method invoked that should be only be invoked inside a doPrivileged block | INFO | CODE_SMELL |
| findbugs:EC_ARRAY_AND_NONARRAY | Correctness - equals() used to compare array and nonarray | MAJOR | BUG |
| findbugs:EC_BAD_ARRAY_COMPARE | Correctness - Invocation of equals() on an array, which is equivalent to == | MAJOR | BUG |
| findbugs:EC_INCOMPATIBLE_ARRAY_COMPARE | Correctness - equals(...) used to compare incompatible arrays | MAJOR | BUG |
| findbugs:EC_NULL_ARG | Correctness - Call to equals(null) | MAJOR | BUG |
| findbugs:EC_UNRELATED_CLASS_AND_INTERFACE | Correctness - Call to equals() comparing unrelated class and interface | MAJOR | BUG |
| findbugs:EC_UNRELATED_INTERFACES | Correctness - Call to equals() comparing different interface types | MAJOR | BUG |
| findbugs:EC_UNRELATED_TYPES | Correctness - Call to equals() comparing different types | MAJOR | BUG |
| findbugs:EC_UNRELATED_TYPES_USING_POINTER_EQUALITY | Correctness - Using pointer equality to compare different types | MAJOR | BUG |
| findbugs:EI_EXPOSE_BUF | Malicious code - May expose internal representation by returning a buffer sharing non-public data | INFO | CODE_SMELL |
| findbugs:EI_EXPOSE_BUF2 | Malicious code - May expose internal representation by creating a buffer which incorporates reference to array | INFO | CODE_SMELL |
| findbugs:EI_EXPOSE_STATIC_BUF2 | Malicious code - May expose internal static state by creating a buffer which stores an external array into a static field | INFO | CODE_SMELL |
| findbugs:EI_EXPOSE_STATIC_REP2 | Malicious code - May expose internal static state by storing a mutable object into a static field | INFO | CODE_SMELL |
| findbugs:ENV_USE_PROPERTY_INSTEAD_OF_ENV | Bad practice - It is preferable to use portable Java property instead of environment variable. | MAJOR | CODE_SMELL |
| findbugs:EOS_BAD_END_OF_STREAM_CHECK | Correctness - Data read is converted before comparison to -1 | MAJOR | BUG |
| findbugs:EQ_ABSTRACT_SELF | Bad practice - Abstract class defines covariant equals() method | MAJOR | CODE_SMELL |
| findbugs:EQ_ALWAYS_FALSE | Correctness - equals method always returns false | MAJOR | BUG |
| findbugs:EQ_ALWAYS_TRUE | Correctness - equals method always returns true | MAJOR | BUG |
| findbugs:EQ_CHECK_FOR_OPERAND_NOT_COMPATIBLE_WITH_THIS | Bad practice - Equals checks for incompatible operand | MAJOR | CODE_SMELL |
| findbugs:EQ_COMPARETO_USE_OBJECT_EQUALS | Bad practice - Class defines compareTo(...) and uses Object.equals() | MAJOR | CODE_SMELL |
| findbugs:EQ_COMPARING_CLASS_NAMES | Correctness - equals method compares class names rather than class objects | MAJOR | BUG |
| findbugs:EQ_DOESNT_OVERRIDE_EQUALS | Style - Class doesn't override equals in superclass | INFO | CODE_SMELL |
| findbugs:EQ_DONT_DEFINE_EQUALS_FOR_ENUM | Correctness - Covariant equals() method defined for enum | MAJOR | BUG |
| findbugs:EQ_GETCLASS_AND_CLASS_CONSTANT | Bad practice - equals method fails for subtypes | MAJOR | CODE_SMELL |
| findbugs:EQ_OTHER_NO_OBJECT | Correctness - equals() method defined that doesn't override equals(Object) | MAJOR | BUG |
| findbugs:EQ_OTHER_USE_OBJECT | Correctness - equals() method defined that doesn't override Object.equals(Object) | MAJOR | BUG |
| findbugs:EQ_OVERRIDING_EQUALS_NOT_SYMMETRIC | Correctness - equals method overrides equals in superclass and may not be symmetric | MAJOR | BUG |
| findbugs:EQ_SELF_NO_OBJECT | Bad practice - Covariant equals() method defined | MAJOR | CODE_SMELL |
| findbugs:EQ_SELF_USE_OBJECT | Correctness - Covariant equals() method defined, Object.equals(Object) inherited | MAJOR | BUG |
| findbugs:EQ_UNUSUAL | Style - Unusual equals method | INFO | CODE_SMELL |
| findbugs:ES_COMPARING_PARAMETER_STRING_WITH_EQ | Bad practice - Comparison of String parameter using == or != | MAJOR | CODE_SMELL |
| findbugs:ES_COMPARING_STRINGS_WITH_EQ | Bad practice - Comparison of String objects using == or != | MAJOR | CODE_SMELL |
| findbugs:ESync_EMPTY_SYNC | Multi-threading - Empty synchronized block | MAJOR | BUG |
| findbugs:FB_MISSING_EXPECTED_WARNING | Correctness - Missing expected or desired warning from SpotBugs | MAJOR | BUG |
| findbugs:FB_UNEXPECTED_WARNING | Correctness - Unexpected/undesired warning from SpotBugs | MAJOR | BUG |
| findbugs:FE_FLOATING_POINT_EQUALITY | Style - Test for floating point equality | INFO | CODE_SMELL |
| findbugs:FE_TEST_IF_EQUAL_TO_NOT_A_NUMBER | Correctness - Doomed test for equality to NaN | MAJOR | BUG |
| findbugs:FI_EMPTY | Bad practice - Empty finalizer should be deleted | MAJOR | CODE_SMELL |
| findbugs:FI_EXPLICIT_INVOCATION | Bad practice - Explicit invocation of finalizer | MAJOR | CODE_SMELL |
| findbugs:FI_FINALIZER_NULLS_FIELDS | Bad practice - Finalizer nulls fields | MAJOR | CODE_SMELL |
| findbugs:FI_FINALIZER_ONLY_NULLS_FIELDS | Bad practice - Finalizer only nulls fields | MAJOR | CODE_SMELL |
| findbugs:FI_MISSING_SUPER_CALL | Bad practice - Finalizer does not call superclass finalizer | MAJOR | CODE_SMELL |
| findbugs:FI_NULLIFY_SUPER | Bad practice - Finalizer nullifies superclass finalizer | MAJOR | CODE_SMELL |
| findbugs:FI_PUBLIC_SHOULD_BE_PROTECTED | Malicious code - Finalizer should be protected, not public | INFO | CODE_SMELL |
| findbugs:FI_USELESS | Bad practice - Finalizer does nothing but call superclass finalizer | MAJOR | CODE_SMELL |
| findbugs:FL_FLOATS_AS_LOOP_COUNTERS | Correctness - Do not use floating-point variables as loop counters | MAJOR | BUG |
| findbugs:FL_MATH_USING_FLOAT_PRECISION | Correctness - Method performs math using floating point precision | MAJOR | BUG |
| findbugs:FS_BAD_DATE_FORMAT_FLAG_COMBO | Bad practice - Date-format strings may lead to unexpected behavior | MAJOR | CODE_SMELL |
| findbugs:GC_UNCHECKED_TYPE_IN_GENERIC_CALL | Bad practice - Unchecked type in generic call | MAJOR | CODE_SMELL |
| findbugs:GC_UNRELATED_TYPES | Correctness - No relationship between generic parameter and method argument | MAJOR | BUG |
| findbugs:HE_EQUALS_NO_HASHCODE | Bad practice - Class defines equals() but not hashCode() | MAJOR | CODE_SMELL |
| findbugs:HE_EQUALS_USE_HASHCODE | Bad practice - Class defines equals() and uses Object.hashCode() | MAJOR | CODE_SMELL |
| findbugs:HE_HASHCODE_NO_EQUALS | Bad practice - Class defines hashCode() but not equals() | MAJOR | CODE_SMELL |
| findbugs:HE_HASHCODE_USE_OBJECT_EQUALS | Bad practice - Class defines hashCode() and uses Object.equals() | MAJOR | CODE_SMELL |
| findbugs:HE_INHERITS_EQUALS_USE_HASHCODE | Bad practice - Class inherits equals() and uses Object.hashCode() | MAJOR | CODE_SMELL |
| findbugs:HE_SIGNATURE_DECLARES_HASHING_OF_UNHASHABLE_CLASS | Correctness - Signature declares use of unhashable class in hashed construct | MAJOR | BUG |
| findbugs:HE_USE_OF_UNHASHABLE_CLASS | Correctness - Use of class without a hashCode() method in a hashed data structure | MAJOR | BUG |
| findbugs:HRS_REQUEST_PARAMETER_TO_COOKIE | Security - HTTP cookie formed from untrusted input | MAJOR | VULNERABILITY |
| findbugs:HRS_REQUEST_PARAMETER_TO_HTTP_HEADER | Security - HTTP Response splitting vulnerability | MAJOR | VULNERABILITY |
| findbugs:HSC_HUGE_SHARED_STRING_CONSTANT | Performance - Huge string constants is duplicated across multiple class files | MAJOR | BUG |
| findbugs:HSM_HIDING_METHOD | Correctness - Method hiding should be avoided. | MAJOR | BUG |
| findbugs:IA_AMBIGUOUS_INVOCATION_OF_INHERITED_OR_OUTER_METHOD | Style - Potentially ambiguous invocation of either an inherited or outer method | INFO | CODE_SMELL |
| findbugs:ICAST_BAD_SHIFT_AMOUNT | Correctness - 32 bit int shifted by an amount not in the range -31..31 | MAJOR | BUG |
| findbugs:ICAST_IDIV_CAST_TO_DOUBLE | Style - Integral division result cast to double or float | INFO | CODE_SMELL |
| findbugs:ICAST_INTEGER_MULTIPLY_CAST_TO_LONG | Style - Result of integer multiplication cast to long | INFO | CODE_SMELL |
| findbugs:ICAST_INT_2_LONG_AS_INSTANT | Correctness - int value converted to long and used as absolute time | MAJOR | BUG |
| findbugs:ICAST_INT_CAST_TO_DOUBLE_PASSED_TO_CEIL | Correctness - Integral value cast to double and then passed to Math.ceil | MAJOR | BUG |
| findbugs:ICAST_INT_CAST_TO_FLOAT_PASSED_TO_ROUND | Correctness - int value cast to float and then passed to Math.round | MAJOR | BUG |
| findbugs:ICAST_QUESTIONABLE_UNSIGNED_RIGHT_SHIFT | Style - Unsigned right shift cast to short/byte | INFO | CODE_SMELL |
| findbugs:IC_INIT_CIRCULARITY | Style - Initialization circularity | INFO | CODE_SMELL |
| findbugs:IC_SUPERCLASS_USES_SUBCLASS_DURING_INITIALIZATION | Bad practice - Superclass uses subclass during initialization | MAJOR | CODE_SMELL |
| findbugs:IIL_ELEMENTS_GET_LENGTH_IN_LOOP | Performance - NodeList.getLength() called in a loop | MAJOR | BUG |
| findbugs:IIL_PATTERN_COMPILE_IN_LOOP | Performance - Method calls Pattern.compile in a loop | MAJOR | BUG |
| findbugs:IIL_PATTERN_COMPILE_IN_LOOP_INDIRECT | Performance - Method compiles the regular expression in a loop | MAJOR | BUG |
| findbugs:IIL_PREPARE_STATEMENT_IN_LOOP | Performance - Method calls prepareStatement in a loop | MAJOR | BUG |
| findbugs:IIO_INEFFICIENT_INDEX_OF | Performance - Inefficient use of String.indexOf(String) | MAJOR | BUG |
| findbugs:IIO_INEFFICIENT_LAST_INDEX_OF | Performance - Inefficient use of String.lastIndexOf(String) | MAJOR | BUG |
| findbugs:IJU_ASSERT_METHOD_INVOKED_FROM_RUN_METHOD | Correctness - JUnit assertion in run method will not be noticed by JUnit | MAJOR | BUG |
| findbugs:IJU_BAD_SUITE_METHOD | Correctness - TestCase declares a bad suite method | MAJOR | BUG |
| findbugs:IJU_NO_TESTS | Correctness - TestCase has no tests | MAJOR | BUG |
| findbugs:IJU_SETUP_NO_SUPER | Correctness - TestCase defines setUp that doesn't call super.setUp() | MAJOR | BUG |
| findbugs:IJU_SUITE_NOT_STATIC | Correctness - TestCase implements a non-static suite method | MAJOR | BUG |
| findbugs:IJU_TEARDOWN_NO_SUPER | Correctness - TestCase defines tearDown that doesn't call super.tearDown() | MAJOR | BUG |
| findbugs:IL_CONTAINER_ADDED_TO_ITSELF | Correctness - A collection is added to itself | MAJOR | BUG |
| findbugs:IL_INFINITE_LOOP | Correctness - An apparent infinite loop | MAJOR | BUG |
| findbugs:IL_INFINITE_RECURSIVE_LOOP | Correctness - An apparent infinite recursive loop | MAJOR | BUG |
| findbugs:IMA_INEFFICIENT_MEMBER_ACCESS | Performance - Method accesses a private member variable of owning class | MAJOR | BUG |
| findbugs:IMSE_DONT_CATCH_IMSE | Bad practice - Dubious catching of IllegalMonitorStateException | MAJOR | CODE_SMELL |
| findbugs:IM_AVERAGE_COMPUTATION_COULD_OVERFLOW | Style - Computation of average could overflow | INFO | CODE_SMELL |
| findbugs:IM_BAD_CHECK_FOR_ODD | Style - Check for oddness that won't work for negative numbers | INFO | CODE_SMELL |
| findbugs:IM_MULTIPLYING_RESULT_OF_IREM | Correctness - Integer multiply of result of integer remainder | MAJOR | BUG |
| findbugs:INT_BAD_COMPARISON_WITH_INT_VALUE | Correctness - Bad comparison of int value with long constant | MAJOR | BUG |
| findbugs:INT_BAD_COMPARISON_WITH_NONNEGATIVE_VALUE | Correctness - Bad comparison of nonnegative value with negative constant or zero | MAJOR | BUG |
| findbugs:INT_BAD_COMPARISON_WITH_SIGNED_BYTE | Correctness - Bad comparison of signed byte | MAJOR | BUG |
| findbugs:INT_BAD_REM_BY_1 | Style - Integer remainder modulo 1 | INFO | CODE_SMELL |
| findbugs:INT_VACUOUS_BIT_OPERATION | Style - Vacuous bit mask operation on integer value | INFO | CODE_SMELL |
| findbugs:INT_VACUOUS_COMPARISON | Style - Vacuous comparison of integer value | INFO | CODE_SMELL |
| findbugs:IO_APPENDING_TO_OBJECT_OUTPUT_STREAM | Correctness - Doomed attempt to append to an object output stream | MAJOR | BUG |
| findbugs:IP_PARAMETER_IS_DEAD_BUT_OVERWRITTEN | Correctness - A parameter is dead upon entry to a method but overwritten | MAJOR | BUG |
| findbugs:ISC_INSTANTIATE_STATIC_CLASS | Bad practice - Needless instantiation of class that only supplies static methods | MAJOR | CODE_SMELL |
| findbugs:IS_FIELD_NOT_GUARDED | Multi-threading - Field not guarded against concurrent access | MAJOR | BUG |
| findbugs:ITA_INEFFICIENT_TO_ARRAY | Performance - Method uses toArray() with zero-length array argument | MAJOR | BUG |
| findbugs:IT_NO_SUCH_ELEMENT | Bad practice - Iterator next() method cannot throw NoSuchElementException | MAJOR | CODE_SMELL |
| findbugs:J2EE_STORE_OF_NON_SERIALIZABLE_OBJECT_INTO_SESSION | Bad practice - Store of non serializable object into HttpSession | MAJOR | CODE_SMELL |
| findbugs:JCIP_FIELD_ISNT_FINAL_IN_IMMUTABLE_CLASS | Bad practice - Fields of immutable classes should be final | MAJOR | CODE_SMELL |
| findbugs:JLM_JSR166_LOCK_MONITORENTER | Multi-threading - Synchronization performed on Lock | MAJOR | BUG |
| findbugs:JLM_JSR166_UTILCONCURRENT_MONITORENTER | Multi-threading - Synchronization performed on util.concurrent instance | MAJOR | BUG |
| findbugs:JML_JSR166_CALLING_WAIT_RATHER_THAN_AWAIT | Multi-threading - Using monitor style wait methods on util.concurrent abstraction | MAJOR | BUG |
| findbugs:JUA_DONT_ASSERT_INSTANCEOF_IN_TESTS | Bad practice -  Asserting value of instanceof in tests is not recommended. | MAJOR | CODE_SMELL |
| findbugs:LG_LOST_LOGGER_DUE_TO_WEAK_REFERENCE | Experimental - Potential lost logger changes due to weak reference in OpenJDK | INFO | CODE_SMELL |
| findbugs:LI_LAZY_INIT_STATIC | Multi-threading - Incorrect lazy initialization of static field | MAJOR | BUG |
| findbugs:LI_LAZY_INIT_UPDATE_STATIC | Multi-threading - Incorrect lazy initialization and update of static field | MAJOR | BUG |
| findbugs:MC_OVERRIDABLE_METHOD_CALL_IN_CLONE | Malicious code - An overridable method is called from the clone() method. | INFO | CODE_SMELL |
| findbugs:MC_OVERRIDABLE_METHOD_CALL_IN_CONSTRUCTOR | Malicious code - An overridable method is called from a constructor | INFO | CODE_SMELL |
| findbugs:MC_OVERRIDABLE_METHOD_CALL_IN_READ_OBJECT | Malicious code - An overridable method is called from the readObject method. | INFO | CODE_SMELL |
| findbugs:ME_ENUM_FIELD_SETTER | Bad practice - Public enum method unconditionally sets its field | MAJOR | CODE_SMELL |
| findbugs:ME_MUTABLE_ENUM_FIELD | Bad practice - Enum field is public and mutable | MAJOR | CODE_SMELL |
| findbugs:MF_CLASS_MASKS_FIELD | Correctness - Class defines field that masks a superclass field | MAJOR | BUG |
| findbugs:MF_METHOD_MASKS_FIELD | Correctness - Method defines a variable that obscures a field | MAJOR | BUG |
| findbugs:ML_SYNC_ON_FIELD_TO_GUARD_CHANGING_THAT_FIELD | Multi-threading - Synchronization on field in futile attempt to guard that field | MAJOR | BUG |
| findbugs:ML_SYNC_ON_UPDATED_FIELD | Multi-threading - Method synchronizes on an updated field | MAJOR | BUG |
| findbugs:MSF_MUTABLE_SERVLET_FIELD | Multi-threading - Mutable servlet field | MAJOR | BUG |
| findbugs:MS_CANNOT_BE_FINAL | Malicious code - Field isn't final and cannot be protected from malicious code | INFO | CODE_SMELL |
| findbugs:MS_EXPOSE_BUF | Malicious code - May expose internal representation by returning a buffer sharing non-public data | INFO | CODE_SMELL |
| findbugs:MS_FINAL_PKGPROTECT | Malicious code - Field should be both final and package protected | INFO | CODE_SMELL |
| findbugs:MS_MUTABLE_ARRAY | Malicious code - Field is a mutable array | INFO | CODE_SMELL |
| findbugs:MS_MUTABLE_COLLECTION | Malicious code - Field is a mutable collection | INFO | CODE_SMELL |
| findbugs:MS_MUTABLE_COLLECTION_PKGPROTECT | Malicious code - Field is a mutable collection which should be package protected | INFO | CODE_SMELL |
| findbugs:MS_MUTABLE_HASHTABLE | Malicious code - Field is a mutable Hashtable | INFO | CODE_SMELL |
| findbugs:MS_OOI_PKGPROTECT | Malicious code - Field should be moved out of an interface and made package protected | INFO | CODE_SMELL |
| findbugs:MS_PKGPROTECT | Malicious code - Field should be package protected | INFO | CODE_SMELL |
| findbugs:MS_SHOULD_BE_FINAL | Malicious code - Field isn't final but should be | INFO | CODE_SMELL |
| findbugs:MS_SHOULD_BE_REFACTORED_TO_BE_FINAL | Malicious code - Field isn't final but should be refactored to be so | INFO | CODE_SMELL |
| findbugs:MTIA_SUSPECT_SERVLET_INSTANCE_FIELD | Style - Class extends Servlet class and uses instance variables | INFO | CODE_SMELL |
| findbugs:MTIA_SUSPECT_STRUTS_INSTANCE_FIELD | Style - Class extends Struts Action class and uses instance variables | INFO | CODE_SMELL |
| findbugs:MWN_MISMATCHED_NOTIFY | Multi-threading - Mismatched notify() | MAJOR | BUG |
| findbugs:MWN_MISMATCHED_WAIT | Multi-threading - Mismatched wait() | MAJOR | BUG |
| findbugs:NM_BAD_EQUAL | Correctness - Class defines equal(Object); should it be equals(Object)? | MAJOR | BUG |
| findbugs:NM_CLASS_NAMING_CONVENTION | Bad practice - Class names should start with an upper case letter | MAJOR | CODE_SMELL |
| findbugs:NM_CLASS_NOT_EXCEPTION | Bad practice - Class is not derived from an Exception, even though it is named as such | MAJOR | CODE_SMELL |
| findbugs:NM_CONFUSING | Bad practice - Confusing method names | MAJOR | CODE_SMELL |
| findbugs:NM_FIELD_NAMING_CONVENTION | Bad practice - Non-final field names should start with a lower case letter, final fields should be uppercase with words separated by underscores | MAJOR | CODE_SMELL |
| findbugs:NM_FUTURE_KEYWORD_USED_AS_IDENTIFIER | Bad practice - Use of identifier that is a keyword in later versions of Java | MAJOR | CODE_SMELL |
| findbugs:NM_FUTURE_KEYWORD_USED_AS_MEMBER_IDENTIFIER | Bad practice - Use of identifier that is a keyword in later versions of Java | MAJOR | CODE_SMELL |
| findbugs:NM_LCASE_HASHCODE | Correctness - Class defines hashcode(); should it be hashCode()? | MAJOR | BUG |
| findbugs:NM_LCASE_TOSTRING | Correctness - Class defines tostring(); should it be toString()? | MAJOR | BUG |
| findbugs:NM_METHOD_CONSTRUCTOR_CONFUSION | Correctness - Apparent method/constructor confusion | MAJOR | BUG |
| findbugs:NM_METHOD_NAMING_CONVENTION | Bad practice - Method names should start with a lower case letter | MAJOR | CODE_SMELL |
| findbugs:NM_SAME_SIMPLE_NAME_AS_INTERFACE | Bad practice - Class names shouldn't shadow simple name of implemented interface | MAJOR | CODE_SMELL |
| findbugs:NM_SAME_SIMPLE_NAME_AS_SUPERCLASS | Bad practice - Class names shouldn't shadow simple name of superclass | MAJOR | CODE_SMELL |
| findbugs:NM_VERY_CONFUSING | Correctness - Very confusing method names | MAJOR | BUG |
| findbugs:NM_VERY_CONFUSING_INTENTIONAL | Bad practice - Very confusing method names (but perhaps intentional) | MAJOR | CODE_SMELL |
| findbugs:NM_WRONG_PACKAGE | Correctness - Method doesn't override method in superclass due to wrong package for parameter | MAJOR | BUG |
| findbugs:NM_WRONG_PACKAGE_INTENTIONAL | Bad practice - Method doesn't override method in superclass due to wrong package for parameter | MAJOR | CODE_SMELL |
| findbugs:NN_NAKED_NOTIFY | Multi-threading - Naked notify | MAJOR | BUG |
| findbugs:NO_NOTIFY_NOT_NOTIFYALL | Multi-threading - Using notify() rather than notifyAll() | MAJOR | BUG |
| findbugs:NP_ALWAYS_NULL | Correctness - Null pointer dereference | MAJOR | BUG |
| findbugs:NP_ALWAYS_NULL_EXCEPTION | Correctness - Null pointer dereference in method on exception path | MAJOR | BUG |
| findbugs:NP_ARGUMENT_MIGHT_BE_NULL | Correctness - Method does not check for null argument | MAJOR | BUG |
| findbugs:NP_BOOLEAN_RETURN_NULL | Bad practice - Method with Boolean return type returns explicit null | MAJOR | CODE_SMELL |
| findbugs:NP_CLONE_COULD_RETURN_NULL | Bad practice - Clone method may return null | MAJOR | CODE_SMELL |
| findbugs:NP_CLOSING_NULL | Correctness - close() invoked on a value that is always null | MAJOR | BUG |
| findbugs:NP_DEREFERENCE_OF_READLINE_VALUE | Style - Dereference of the result of readLine() without nullcheck | INFO | CODE_SMELL |
| findbugs:NP_EQUALS_SHOULD_HANDLE_NULL_ARGUMENT | Bad practice - equals() method does not check for null argument | MAJOR | CODE_SMELL |
| findbugs:NP_GUARANTEED_DEREF | Correctness - Null value is guaranteed to be dereferenced | MAJOR | BUG |
| findbugs:NP_GUARANTEED_DEREF_ON_EXCEPTION_PATH | Correctness - Value is null and guaranteed to be dereferenced on exception path | MAJOR | BUG |
| findbugs:NP_IMMEDIATE_DEREFERENCE_OF_READLINE | Style - Immediate dereference of the result of readLine() | INFO | CODE_SMELL |
| findbugs:NP_LOAD_OF_KNOWN_NULL_VALUE | Style - Load of known null value | INFO | CODE_SMELL |
| findbugs:NP_METHOD_PARAMETER_RELAXING_ANNOTATION | Style - Method tightens nullness annotation on parameter | INFO | CODE_SMELL |
| findbugs:NP_METHOD_PARAMETER_TIGHTENS_ANNOTATION | Style - Method tightens nullness annotation on parameter | INFO | CODE_SMELL |
| findbugs:NP_METHOD_RETURN_RELAXING_ANNOTATION | Style - Method relaxes nullness annotation on return value | INFO | CODE_SMELL |
| findbugs:NP_NONNULL_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR | Correctness - Non-null field is not initialized | MAJOR | BUG |
| findbugs:NP_NONNULL_PARAM_VIOLATION | Correctness - Method call passes null to a non-null parameter | MAJOR | BUG |
| findbugs:NP_NONNULL_RETURN_VIOLATION | Correctness - Method may return null, but is declared @Nonnull | MAJOR | BUG |
| findbugs:NP_NULL_INSTANCEOF | Correctness - A known null value is checked to see if it is an instance of a type | MAJOR | BUG |
| findbugs:NP_NULL_ON_SOME_PATH_EXCEPTION | Correctness - Possible null pointer dereference in method on exception path | MAJOR | BUG |
| findbugs:NP_NULL_ON_SOME_PATH_MIGHT_BE_INFEASIBLE | Style - Possible null pointer dereference on branch that might be infeasible | INFO | CODE_SMELL |
| findbugs:NP_NULL_PARAM_DEREF | Correctness - Method call passes null for non-null parameter | MAJOR | BUG |
| findbugs:NP_NULL_PARAM_DEREF_ALL_TARGETS_DANGEROUS | Correctness - Method call passes null for non-null parameter | MAJOR | BUG |
| findbugs:NP_NULL_PARAM_DEREF_NONVIRTUAL | Correctness - Non-virtual method call passes null for non-null parameter | MAJOR | BUG |
| findbugs:NP_OPTIONAL_RETURN_NULL | Correctness - Method with Optional return type returns explicit null | MAJOR | BUG |
| findbugs:NP_PARAMETER_MUST_BE_NONNULL_BUT_MARKED_AS_NULLABLE | Style - Parameter must be non-null but is marked as nullable | INFO | CODE_SMELL |
| findbugs:NP_STORE_INTO_NONNULL_FIELD | Correctness - Store of null value into field annotated @Nonnull | MAJOR | BUG |
| findbugs:NP_SYNC_AND_NULL_CHECK_FIELD | Multi-threading - Synchronize and null check on the same field. | MAJOR | BUG |
| findbugs:NP_TOSTRING_COULD_RETURN_NULL | Bad practice - toString method may return null | MAJOR | CODE_SMELL |
| findbugs:NP_UNWRITTEN_FIELD | Correctness - Read of unwritten field | MAJOR | BUG |
| findbugs:NP_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD | Style - Read of unwritten public or protected field | INFO | CODE_SMELL |
| findbugs:NS_DANGEROUS_NON_SHORT_CIRCUIT | Style - Potentially dangerous use of non-short-circuit logic | INFO | CODE_SMELL |
| findbugs:NS_NON_SHORT_CIRCUIT | Style - Questionable use of non-short-circuit logic | INFO | CODE_SMELL |
| findbugs:OBL_UNSATISFIED_OBLIGATION | Experimental - Method may fail to clean up stream or resource | INFO | CODE_SMELL |
| findbugs:OBL_UNSATISFIED_OBLIGATION_EXCEPTION_EDGE | Experimental - Method may fail to clean up stream or resource on checked exception | INFO | CODE_SMELL |
| findbugs:ODR_OPEN_DATABASE_RESOURCE | Bad practice - Method may fail to close database resource | MAJOR | CODE_SMELL |
| findbugs:ODR_OPEN_DATABASE_RESOURCE_EXCEPTION_PATH | Bad practice - Method may fail to close database resource on exception | MAJOR | CODE_SMELL |
| findbugs:OS_OPEN_STREAM | Bad practice - Method may fail to close stream | MAJOR | CODE_SMELL |
| findbugs:OS_OPEN_STREAM_EXCEPTION_PATH | Bad practice - Method may fail to close stream on exception | MAJOR | CODE_SMELL |
| findbugs:OVERRIDING_METHODS_MUST_INVOKE_SUPER | Correctness - Super method is annotated with @OverridingMethodsMustInvokeSuper, but the overriding method isn't calling the super method. | MAJOR | BUG |
| findbugs:PA_PUBLIC_ARRAY_ATTRIBUTE | Bad practice - Array-type field is public | MAJOR | CODE_SMELL |
| findbugs:PA_PUBLIC_MUTABLE_OBJECT_ATTRIBUTE | Bad practice - Mutable object-type field is public | MAJOR | CODE_SMELL |
| findbugs:PA_PUBLIC_PRIMITIVE_ATTRIBUTE | Bad practice - Primitive field is public | MAJOR | CODE_SMELL |
| findbugs:PERM_SUPER_NOT_CALLED_IN_GETPERMISSIONS | Malicious code - Custom class loader does not call its superclass's getPermissions() | INFO | CODE_SMELL |
| findbugs:PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_CLASS_NAMES | Bad practice - Do not reuse public identifiers from JSL as class name | MAJOR | CODE_SMELL |
| findbugs:PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_FIELD_NAMES | Bad practice - Do not reuse public identifiers from JSL as field name | MAJOR | CODE_SMELL |
| findbugs:PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_LOCAL_VARIABLE_NAMES | Bad practice - Do not reuse public identifiers from JSL as method name | MAJOR | CODE_SMELL |
| findbugs:PI_DO_NOT_REUSE_PUBLIC_IDENTIFIERS_METHOD_NAMES | Bad practice - Do not reuse public identifiers from JSL as method name | MAJOR | CODE_SMELL |
| findbugs:PS_PUBLIC_SEMAPHORES | Style - Class exposes synchronization and semaphores in its public interface | INFO | CODE_SMELL |
| findbugs:PT_ABSOLUTE_PATH_TRAVERSAL | Security - Absolute path traversal in servlet | MAJOR | VULNERABILITY |
| findbugs:PT_RELATIVE_PATH_TRAVERSAL | Security - Relative path traversal in servlet | MAJOR | VULNERABILITY |
| findbugs:PZLA_PREFER_ZERO_LENGTH_ARRAYS | Style - Consider returning a zero length array rather than null | INFO | CODE_SMELL |
| findbugs:PZ_DONT_REUSE_ENTRY_OBJECTS_IN_ITERATORS | Bad practice - Don't reuse entry objects in iterators | MAJOR | CODE_SMELL |
| findbugs:QBA_QUESTIONABLE_BOOLEAN_ASSIGNMENT | Correctness - Method assigns boolean literal in boolean expression | MAJOR | BUG |
| findbugs:QF_QUESTIONABLE_FOR_LOOP | Style - Complicated, subtle or wrong increment in for-loop | INFO | CODE_SMELL |
| findbugs:RANGE_ARRAY_INDEX | Correctness - Array index is out of bounds | CRITICAL | BUG |
| findbugs:RANGE_ARRAY_LENGTH | Correctness - Array length is out of bounds | CRITICAL | BUG |
| findbugs:RANGE_ARRAY_OFFSET | Correctness - Array offset is out of bounds | CRITICAL | BUG |
| findbugs:RANGE_STRING_INDEX | Correctness - String index is out of bounds | CRITICAL | BUG |
| findbugs:RCN_REDUNDANT_COMPARISON_OF_NULL_AND_NONNULL_VALUE | Style - Redundant comparison of non-null value to null | INFO | CODE_SMELL |
| findbugs:RCN_REDUNDANT_COMPARISON_TWO_NULL_VALUES | Style - Redundant comparison of two null values | INFO | CODE_SMELL |
| findbugs:RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE | Style - Redundant nullcheck of value known to be non-null | INFO | CODE_SMELL |
| findbugs:RCN_REDUNDANT_NULLCHECK_OF_NULL_VALUE | Style - Redundant nullcheck of value known to be null | INFO | CODE_SMELL |
| findbugs:RCN_REDUNDANT_NULLCHECK_WOULD_HAVE_BEEN_A_NPE | Correctness - Nullcheck of value previously dereferenced | MAJOR | BUG |
| findbugs:RC_REF_COMPARISON | Correctness - Suspicious reference comparison | MAJOR | BUG |
| findbugs:RC_REF_COMPARISON_BAD_PRACTICE | Bad practice - Suspicious reference comparison to constant | MAJOR | CODE_SMELL |
| findbugs:RC_REF_COMPARISON_BAD_PRACTICE_BOOLEAN | Bad practice - Suspicious reference comparison of Boolean values | MAJOR | CODE_SMELL |
| findbugs:REC_CATCH_EXCEPTION | Style - Exception is caught when Exception is not thrown | INFO | CODE_SMELL |
| findbugs:REFLC_REFLECTION_MAY_INCREASE_ACCESSIBILITY_OF_CLASS | Malicious code - Public method uses reflection to create a class it gets in its parameter which could increase the accessibility of any class | INFO | CODE_SMELL |
| findbugs:REFLF_REFLECTION_MAY_INCREASE_ACCESSIBILITY_OF_FIELD | Malicious code - Public method uses reflection to modify a field it gets in its parameter which could increase the accessibility of any class | INFO | CODE_SMELL |
| findbugs:RE_BAD_SYNTAX_FOR_REGULAR_EXPRESSION | Correctness - Invalid syntax for regular expression | MAJOR | BUG |
| findbugs:RE_CANT_USE_FILE_SEPARATOR_AS_REGULAR_EXPRESSION | Correctness - File.separator used for regular expression | MAJOR | BUG |
| findbugs:RE_POSSIBLE_UNINTENDED_PATTERN | Correctness - "." or "/" used for regular expression | MAJOR | BUG |
| findbugs:RI_REDUNDANT_INTERFACES | Style - Class implements same interface as superclass | INFO | CODE_SMELL |
| findbugs:RR_NOT_CHECKED | Bad practice - Method ignores results of InputStream.read() | MAJOR | CODE_SMELL |
| findbugs:RS_READOBJECT_SYNC | Multi-threading - Class's readObject() method is synchronized | MAJOR | BUG |
| findbugs:RU_INVOKE_RUN | Multi-threading - Invokes run on a thread (did you mean to start it instead?) | MAJOR | BUG |
| findbugs:RV_01_TO_INT | Correctness - Random value from 0 to 1 is coerced to the integer 0 | MAJOR | BUG |
| findbugs:RV_ABSOLUTE_VALUE_OF_HASHCODE | Correctness - Bad attempt to compute absolute value of signed 32-bit hashcode | MAJOR | BUG |
| findbugs:RV_ABSOLUTE_VALUE_OF_RANDOM_INT | Correctness - Bad attempt to compute absolute value of signed random integer | MAJOR | BUG |
| findbugs:RV_CHECK_COMPARETO_FOR_SPECIFIC_RETURN_VALUE | Correctness - Code checks for specific values returned by compareTo | MAJOR | BUG |
| findbugs:RV_CHECK_FOR_POSITIVE_INDEXOF | Style - Method checks to see if result of String.indexOf is positive | INFO | CODE_SMELL |
| findbugs:RV_DONT_JUST_NULL_CHECK_READLINE | Style - Method discards result of readLine after checking if it is non-null | INFO | CODE_SMELL |
| findbugs:RV_EXCEPTION_NOT_THROWN | Correctness - Exception created and dropped rather than thrown | MAJOR | BUG |
| findbugs:RV_NEGATING_RESULT_OF_COMPARETO | Bad practice - Negating the result of compareTo()/compare() | MAJOR | CODE_SMELL |
| findbugs:RV_REM_OF_HASHCODE | Style - Remainder of hashCode could be negative | INFO | CODE_SMELL |
| findbugs:RV_REM_OF_RANDOM_INT | Style - Remainder of 32-bit signed random integer | INFO | CODE_SMELL |
| findbugs:RV_RETURN_VALUE_IGNORED | Correctness - Method ignores return value | MAJOR | BUG |
| findbugs:RV_RETURN_VALUE_IGNORED_BAD_PRACTICE | Bad practice - Method ignores exceptional return value | MAJOR | CODE_SMELL |
| findbugs:RV_RETURN_VALUE_IGNORED_INFERRED | Style - Method ignores return value, is this OK? | INFO | CODE_SMELL |
| findbugs:RV_RETURN_VALUE_OF_PUTIFABSENT_IGNORED | Multi-threading - Return value of putIfAbsent ignored, value passed to putIfAbsent reused | MAJOR | BUG |
| findbugs:RpC_REPEATED_CONDITIONAL_TEST | Correctness - Repeated conditional tests | MAJOR | BUG |
| findbugs:SA_FIELD_DOUBLE_ASSIGNMENT | Style - Double assignment of field | INFO | CODE_SMELL |
| findbugs:SA_FIELD_SELF_ASSIGNMENT | Correctness - Self assignment of field | MAJOR | BUG |
| findbugs:SA_FIELD_SELF_COMPARISON | Correctness - Self comparison of field with itself | MAJOR | BUG |
| findbugs:SA_FIELD_SELF_COMPUTATION | Correctness - Nonsensical self computation involving a field (e.g., x & x) | MAJOR | BUG |
| findbugs:SA_LOCAL_DOUBLE_ASSIGNMENT | Style - Double assignment of local variable | INFO | CODE_SMELL |
| findbugs:SA_LOCAL_SELF_ASSIGNMENT | Style - Self assignment of local variable | INFO | CODE_SMELL |
| findbugs:SA_LOCAL_SELF_ASSIGNMENT_INSTEAD_OF_FIELD | Correctness - Self assignment of local rather than assignment to field | MAJOR | BUG |
| findbugs:SA_LOCAL_SELF_COMPARISON | Correctness - Self comparison of value with itself | MAJOR | BUG |
| findbugs:SA_LOCAL_SELF_COMPUTATION | Correctness - Nonsensical self computation involving a variable (e.g., x & x) | MAJOR | BUG |
| findbugs:SC_START_IN_CTOR | Multi-threading - Constructor invokes Thread.start() | MAJOR | BUG |
| findbugs:SE_BAD_FIELD | Bad practice - Non-transient non-serializable instance field in serializable class | MAJOR | CODE_SMELL |
| findbugs:SE_BAD_FIELD_INNER_CLASS | Bad practice - Non-serializable class has a serializable inner class | MAJOR | CODE_SMELL |
| findbugs:SE_BAD_FIELD_STORE | Bad practice - Non-serializable value stored into instance field of a serializable class | MAJOR | CODE_SMELL |
| findbugs:SE_COMPARATOR_SHOULD_BE_SERIALIZABLE | Bad practice - Comparator doesn't implement Serializable | MAJOR | CODE_SMELL |
| findbugs:SE_INNER_CLASS | Bad practice - Serializable inner class | MAJOR | CODE_SMELL |
| findbugs:SE_METHOD_MUST_BE_PRIVATE | Correctness - Method must be private in order for serialization to work | MAJOR | BUG |
| findbugs:SE_NONFINAL_SERIALVERSIONID | Bad practice - serialVersionUID isn't final | MAJOR | CODE_SMELL |
| findbugs:SE_NONLONG_SERIALVERSIONID | Bad practice - serialVersionUID isn't long | MAJOR | CODE_SMELL |
| findbugs:SE_NONSTATIC_SERIALVERSIONID | Bad practice - serialVersionUID isn't static | MAJOR | CODE_SMELL |
| findbugs:SE_NO_SERIALVERSIONID | Bad practice - Class is Serializable, but doesn't define serialVersionUID | MAJOR | CODE_SMELL |
| findbugs:SE_NO_SUITABLE_CONSTRUCTOR | Bad practice - Class is Serializable but its superclass doesn't define a void constructor | MAJOR | CODE_SMELL |
| findbugs:SE_NO_SUITABLE_CONSTRUCTOR_FOR_EXTERNALIZATION | Bad practice - Class is Externalizable but doesn't define a void constructor | MAJOR | CODE_SMELL |
| findbugs:SE_PREVENT_EXT_OBJ_OVERWRITE | Bad practice - Prevent overwriting of externalizable objects | MAJOR | CODE_SMELL |
| findbugs:SE_PRIVATE_READ_RESOLVE_NOT_INHERITED | Style - Private readResolve method not inherited by subclasses | INFO | CODE_SMELL |
| findbugs:SE_READ_RESOLVE_IS_STATIC | Correctness - The readResolve method must not be declared as a static method. | MAJOR | BUG |
| findbugs:SE_READ_RESOLVE_MUST_RETURN_OBJECT | Bad practice - The readResolve method must be declared with a return type of Object. | MAJOR | CODE_SMELL |
| findbugs:SE_TRANSIENT_FIELD_NOT_RESTORED | Bad practice - Transient field that isn't set by deserialization. | MAJOR | CODE_SMELL |
| findbugs:SE_TRANSIENT_FIELD_OF_NONSERIALIZABLE_CLASS | Style - Transient field of class that isn't Serializable. | INFO | CODE_SMELL |
| findbugs:SF_DEAD_STORE_DUE_TO_SWITCH_FALLTHROUGH | Correctness - Dead store due to switch statement fall through | MAJOR | BUG |
| findbugs:SF_DEAD_STORE_DUE_TO_SWITCH_FALLTHROUGH_TO_THROW | Correctness - Dead store due to switch statement fall through to throw | MAJOR | BUG |
| findbugs:SF_SWITCH_FALLTHROUGH | Style - Switch statement found where one case falls through to the next case | INFO | CODE_SMELL |
| findbugs:SIC_INNER_SHOULD_BE_STATIC | Performance - Should be a static inner class | MAJOR | BUG |
| findbugs:SIC_INNER_SHOULD_BE_STATIC_ANON | Performance - Could be refactored into a named static inner class | MAJOR | BUG |
| findbugs:SIC_INNER_SHOULD_BE_STATIC_NEEDS_THIS | Performance - Could be refactored into a static inner class | MAJOR | BUG |
| findbugs:SIC_THREADLOCAL_DEADLY_EMBRACE | Correctness - Deadly embrace of non-static inner class and thread local | MAJOR | BUG |
| findbugs:SING_SINGLETON_GETTER_NOT_SYNCHRONIZED | Correctness - Instance-getter method of class using singleton design pattern is not synchronized. | MAJOR | BUG |
| findbugs:SING_SINGLETON_HAS_NONPRIVATE_CONSTRUCTOR | Correctness - Class using singleton design pattern has non-private constructor. | MAJOR | BUG |
| findbugs:SING_SINGLETON_IMPLEMENTS_CLONEABLE | Correctness - Class using singleton design pattern directly implements Cloneable interface. | MAJOR | BUG |
| findbugs:SING_SINGLETON_IMPLEMENTS_CLONE_METHOD | Correctness - Class using singleton design pattern implements clone() method without being an unconditional CloneNotSupportedException-thrower. | MAJOR | BUG |
| findbugs:SING_SINGLETON_IMPLEMENTS_SERIALIZABLE | Correctness - Class using singleton design pattern directly or indirectly implements Serializable interface. | MAJOR | BUG |
| findbugs:SING_SINGLETON_INDIRECTLY_IMPLEMENTS_CLONEABLE | Correctness - Class using singleton design pattern indirectly implements Cloneable interface. | MAJOR | BUG |
| findbugs:SIO_SUPERFLUOUS_INSTANCEOF | Correctness - Unnecessary type check done using instanceof operator | MAJOR | BUG |
| findbugs:SI_INSTANCE_BEFORE_FINALS_ASSIGNED | Bad practice - Static initializer creates instance before all static final fields assigned | MAJOR | CODE_SMELL |
| findbugs:SKIPPED_CLASS_TOO_BIG | Experimental - Class too big for analysis | INFO | CODE_SMELL |
| findbugs:SP_SPIN_ON_FIELD | Multi-threading - Method spins on field | MAJOR | BUG |
| findbugs:SQL_BAD_PREPARED_STATEMENT_ACCESS | Correctness - Method attempts to access a prepared statement parameter with index 0 | MAJOR | BUG |
| findbugs:SQL_BAD_RESULTSET_ACCESS | Correctness - Method attempts to access a result set field with index 0 | MAJOR | BUG |
| findbugs:SQL_NONCONSTANT_STRING_PASSED_TO_EXECUTE | Security - Nonconstant string passed to execute or addBatch method on an SQL statement | MAJOR | VULNERABILITY |
| findbugs:SQL_PREPARED_STATEMENT_GENERATED_FROM_NONCONSTANT_STRING | Security - A prepared statement is generated from a nonconstant String | MAJOR | VULNERABILITY |
| findbugs:SR_NOT_CHECKED | Bad practice - Method ignores results of InputStream.skip() | MAJOR | CODE_SMELL |
| findbugs:SSD_DO_NOT_USE_INSTANCE_LOCK_ON_SHARED_STATIC_DATA | Multi-threading - Instance level lock was used on a shared static data | MAJOR | BUG |
| findbugs:STCAL_INVOKE_ON_STATIC_CALENDAR_INSTANCE | Multi-threading - Call to static Calendar | MAJOR | BUG |
| findbugs:STCAL_INVOKE_ON_STATIC_DATE_FORMAT_INSTANCE | Multi-threading - Call to static DateFormat | MAJOR | BUG |
| findbugs:STCAL_STATIC_CALENDAR_INSTANCE | Multi-threading - Static Calendar field | MAJOR | BUG |
| findbugs:STCAL_STATIC_SIMPLE_DATE_FORMAT_INSTANCE | Multi-threading - Static DateFormat | MAJOR | BUG |
| findbugs:STI_INTERRUPTED_ON_CURRENTTHREAD | Correctness - Unneeded use of currentThread() call, to call interrupted() | MAJOR | BUG |
| findbugs:STI_INTERRUPTED_ON_UNKNOWNTHREAD | Correctness - Static Thread.interrupted() method invoked on thread instance | MAJOR | BUG |
| findbugs:SWL_SLEEP_WITH_LOCK_HELD | Multi-threading - Method calls Thread.sleep() with a lock held | MAJOR | BUG |
| findbugs:SW_SWING_METHODS_INVOKED_IN_SWING_THREAD | Bad practice - Certain swing methods need to be invoked in Swing thread | MAJOR | CODE_SMELL |
| findbugs:THROWS_METHOD_THROWS_CLAUSE_BASIC_EXCEPTION | Bad practice - Method lists Exception in its throws clause, but it could be more specific. | MAJOR | CODE_SMELL |
| findbugs:THROWS_METHOD_THROWS_CLAUSE_THROWABLE | Bad practice - Method lists Throwable in its throws clause, but it could be more specific. | MAJOR | CODE_SMELL |
| findbugs:THROWS_METHOD_THROWS_RUNTIMEEXCEPTION | Bad practice - Method intentionally throws RuntimeException. | MAJOR | CODE_SMELL |
| findbugs:TLW_TWO_LOCK_WAIT | Multi-threading - Wait with two locks held | MAJOR | BUG |
| findbugs:TQ_ALWAYS_VALUE_USED_WHERE_NEVER_REQUIRED | Correctness - Value annotated as carrying a type qualifier used where a value that must not carry that qualifier is required | MAJOR | BUG |
| findbugs:TQ_COMPARING_VALUES_WITH_INCOMPATIBLE_TYPE_QUALIFIERS | Correctness - Comparing values with incompatible type qualifiers | MAJOR | BUG |
| findbugs:TQ_EXPLICIT_UNKNOWN_SOURCE_VALUE_REACHES_ALWAYS_SINK | Style - Value required to have type qualifier, but marked as unknown | INFO | CODE_SMELL |
| findbugs:TQ_EXPLICIT_UNKNOWN_SOURCE_VALUE_REACHES_NEVER_SINK | Style - Value required to not have type qualifier, but marked as unknown | INFO | CODE_SMELL |
| findbugs:TQ_MAYBE_SOURCE_VALUE_REACHES_ALWAYS_SINK | Correctness - Value that might not carry a type qualifier is always used in a way requires that type qualifier | MAJOR | BUG |
| findbugs:TQ_MAYBE_SOURCE_VALUE_REACHES_NEVER_SINK | Correctness - Value that might carry a type qualifier is always used in a way prohibits it from having that type qualifier | MAJOR | BUG |
| findbugs:TQ_NEVER_VALUE_USED_WHERE_ALWAYS_REQUIRED | Correctness - Value annotated as never carrying a type qualifier used where value carrying that qualifier is required | MAJOR | BUG |
| findbugs:TQ_UNKNOWN_VALUE_USED_WHERE_ALWAYS_STRICTLY_REQUIRED | Correctness - Value without a type qualifier used where a value is required to have that qualifier | MAJOR | BUG |
| findbugs:UCF_USELESS_CONTROL_FLOW | Style - Useless control flow | INFO | CODE_SMELL |
| findbugs:UCF_USELESS_CONTROL_FLOW_NEXT_LINE | Style - Useless control flow to next line | INFO | CODE_SMELL |
| findbugs:UC_USELESS_CONDITION | Style - Condition has no effect | INFO | CODE_SMELL |
| findbugs:UC_USELESS_CONDITION_TYPE | Style - Condition has no effect due to the variable type | INFO | CODE_SMELL |
| findbugs:UC_USELESS_OBJECT | Style - Useless object created | INFO | CODE_SMELL |
| findbugs:UC_USELESS_OBJECT_STACK | Style - Useless object created on stack | INFO | CODE_SMELL |
| findbugs:UC_USELESS_VOID_METHOD | Style - Useless non-empty void method | INFO | CODE_SMELL |
| findbugs:UG_SYNC_SET_UNSYNC_GET | Multi-threading - Unsynchronized get method, synchronized set method | MAJOR | BUG |
| findbugs:UI_INHERITANCE_UNSAFE_GETRESOURCE | Bad practice - Usage of GetResource may be unsafe if class is extended | MAJOR | CODE_SMELL |
| findbugs:UL_UNRELEASED_LOCK | Multi-threading - Method does not release lock on all paths | MAJOR | BUG |
| findbugs:UL_UNRELEASED_LOCK_EXCEPTION_PATH | Multi-threading - Method does not release lock on all exception paths | MAJOR | BUG |
| findbugs:UMAC_UNCALLABLE_METHOD_OF_ANONYMOUS_CLASS | Correctness - Uncallable method defined in anonymous class | MAJOR | BUG |
| findbugs:UM_UNNECESSARY_MATH | Performance - Method calls static Math class method on a constant value | MAJOR | BUG |
| findbugs:UPM_UNCALLED_PRIVATE_METHOD | Performance - Private method is never called | MAJOR | BUG |
| findbugs:URF_UNREAD_FIELD | Performance - Unread field | MAJOR | BUG |
| findbugs:UR_UNINIT_READ | Correctness - Uninitialized read of field in constructor | MAJOR | BUG |
| findbugs:UR_UNINIT_READ_CALLED_FROM_SUPER_CONSTRUCTOR | Correctness - Uninitialized read of field method called from constructor of superclass | MAJOR | BUG |
| findbugs:USC_POTENTIAL_SECURITY_CHECK_BASED_ON_UNTRUSTED_SOURCE | Malicious code - Potential security check based on untrusted source. | INFO | CODE_SMELL |
| findbugs:USM_USELESS_ABSTRACT_METHOD | Style - Abstract Method is already defined in implemented interface | INFO | CODE_SMELL |
| findbugs:USM_USELESS_SUBCLASS_METHOD | Style - Method superfluously delegates to parent class method | INFO | CODE_SMELL |
| findbugs:US_USELESS_SUPPRESSION_ON_CLASS | Style - Useless suppression on a class | INFO | CODE_SMELL |
| findbugs:US_USELESS_SUPPRESSION_ON_FIELD | Style - Useless suppression on a field | INFO | CODE_SMELL |
| findbugs:US_USELESS_SUPPRESSION_ON_METHOD | Style - Useless suppression on a method | INFO | CODE_SMELL |
| findbugs:US_USELESS_SUPPRESSION_ON_METHOD_PARAMETER | Style - Useless suppression on a method parameter | INFO | CODE_SMELL |
| findbugs:US_USELESS_SUPPRESSION_ON_PACKAGE | Style - Useless suppression on a package | INFO | CODE_SMELL |
| findbugs:UUF_UNUSED_FIELD | Performance - Unused field | MAJOR | BUG |
| findbugs:UUF_UNUSED_PUBLIC_OR_PROTECTED_FIELD | Style - Unused public or protected field | INFO | CODE_SMELL |
| findbugs:UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR | Style - Field not initialized in constructor but dereferenced without null check | INFO | CODE_SMELL |
| findbugs:UWF_UNWRITTEN_FIELD | Correctness - Unwritten field | MAJOR | BUG |
| findbugs:UWF_UNWRITTEN_PUBLIC_OR_PROTECTED_FIELD | Style - Unwritten public or protected field | INFO | CODE_SMELL |
| findbugs:UW_UNCOND_WAIT | Multi-threading - Unconditional wait | MAJOR | BUG |
| findbugs:VA_FORMAT_STRING_USES_NEWLINE | Bad practice - Format string should use %n rather than \n | MAJOR | CODE_SMELL |
| findbugs:VA_PRIMITIVE_ARRAY_PASSED_TO_OBJECT_VARARG | Correctness - Primitive array passed to function expecting a variable number of object arguments | MAJOR | BUG |
| findbugs:VO_VOLATILE_INCREMENT | Multi-threading - An increment to a volatile field isn't atomic | MAJOR | BUG |
| findbugs:VO_VOLATILE_REFERENCE_TO_ARRAY | Multi-threading - A volatile reference to an array doesn't treat the array elements as volatile | MAJOR | BUG |
| findbugs:VR_UNRESOLVABLE_REFERENCE | Correctness - Class makes reference to unresolvable class or method | MAJOR | BUG |
| findbugs:VSC_VULNERABLE_SECURITY_CHECK_METHODS | Malicious code - Non-Private and non-final security check methods are vulnerable | INFO | CODE_SMELL |
| findbugs:WA_AWAIT_NOT_IN_LOOP | Multi-threading - Condition.await() not in loop | MAJOR | BUG |
| findbugs:WA_NOT_IN_LOOP | Multi-threading - Wait not in loop | MAJOR | BUG |
| findbugs:WL_USING_GETCLASS_RATHER_THAN_CLASS_LITERAL | Multi-threading - Synchronization on getClass rather than class literal | MAJOR | BUG |
| findbugs:WMI_WRONG_MAP_ITERATOR | Performance - Inefficient use of keySet iterator instead of entrySet iterator | MAJOR | BUG |
| findbugs:WS_WRITEOBJECT_SYNC | Multi-threading - Class's writeObject() method is synchronized but nothing else is | MAJOR | BUG |
| findbugs:XFB_XML_FACTORY_BYPASS | Style - Method directly allocates a specific implementation of xml interfaces | INFO | CODE_SMELL |
| findbugs:XSS_REQUEST_PARAMETER_TO_SEND_ERROR | Security - Servlet reflected cross site scripting vulnerability in error page | MAJOR | VULNERABILITY |
| findbugs:XSS_REQUEST_PARAMETER_TO_SERVLET_WRITER | Security - Servlet reflected cross site scripting vulnerability | MAJOR | VULNERABILITY |
| findsecbugs:ANDROID_BROADCAST | Security - Broadcast (Android) | INFO | VULNERABILITY |
| findsecbugs:ANDROID_GEOLOCATION | Security - WebView with geolocation activated (Android) | INFO | VULNERABILITY |
| findsecbugs:ANDROID_WEB_VIEW_JAVASCRIPT | Security - WebView with JavaScript enabled (Android) | INFO | VULNERABILITY |
| findsecbugs:ANDROID_WEB_VIEW_JAVASCRIPT_INTERFACE | Security - WebView with JavaScript interface (Android) | INFO | VULNERABILITY |
| findsecbugs:ANDROID_WORLD_WRITABLE | Security - World writable file (Android) | MAJOR | VULNERABILITY |
| findsecbugs:AWS_QUERY_INJECTION | Security - AWS Query Injection | CRITICAL | VULNERABILITY |
| findsecbugs:BEAN_PROPERTY_INJECTION | Security - JavaBeans Property Injection | CRITICAL | VULNERABILITY |
| findsecbugs:BLOWFISH_KEY_SIZE | Security - Blowfish usage with short key | MAJOR | VULNERABILITY |
| findsecbugs:CIPHER_INTEGRITY | Security - Cipher with no integrity | MAJOR | VULNERABILITY |
| findsecbugs:COMMAND_INJECTION | Security - Potential Command Injection | CRITICAL | VULNERABILITY |
| findsecbugs:COOKIE_PERSISTENT | Security - Persistent Cookie Usage | MAJOR | VULNERABILITY |
| findsecbugs:COOKIE_USAGE | Security - Potentially sensitive data in a cookie | INFO | VULNERABILITY |
| findsecbugs:CRLF_INJECTION_LOGS | Security - Potential CRLF Injection for logs | INFO | VULNERABILITY |
| findsecbugs:CUSTOM_MESSAGE_DIGEST | Security - Message digest is custom | MAJOR | VULNERABILITY |
| findsecbugs:DANGEROUS_PERMISSION_COMBINATION | Security - Dangerous combination of permissions granted | CRITICAL | VULNERABILITY |
| findsecbugs:DEFAULT_HTTP_CLIENT | Security - DefaultHttpClient with default constructor is not compatible with TLS 1.2 | MAJOR | VULNERABILITY |
| findsecbugs:DESERIALIZATION_GADGET | Security - This class could be used as deserialization gadget | INFO | VULNERABILITY |
| findsecbugs:DES_USAGE | Security - DES is insecure | MAJOR | VULNERABILITY |
| findsecbugs:ECB_MODE | Security - ECB mode is insecure | MAJOR | VULNERABILITY |
| findsecbugs:EL_INJECTION | Security - Potential code injection when using Expression Language (EL) | CRITICAL | VULNERABILITY |
| findsecbugs:ENTITY_LEAK | Security - Unexpected property leak | MAJOR | VULNERABILITY |
| findsecbugs:ENTITY_MASS_ASSIGNMENT | Security - Mass assignment | MAJOR | VULNERABILITY |
| findsecbugs:ESAPI_ENCRYPTOR | Security - Use of ESAPI Encryptor | INFO | VULNERABILITY |
| findsecbugs:EXTERNAL_CONFIG_CONTROL | Security - Potential external control of configuration | INFO | VULNERABILITY |
| findsecbugs:FILE_UPLOAD_FILENAME | Security - Tainted filename read | INFO | VULNERABILITY |
| findsecbugs:FORMAT_STRING_MANIPULATION | Security - Format String Manipulation | INFO | VULNERABILITY |
| findsecbugs:GROOVY_SHELL | Security - Potential code injection when using GroovyShell | CRITICAL | VULNERABILITY |
| findsecbugs:HARD_CODE_KEY | Security - Hard coded key | MAJOR | VULNERABILITY |
| findsecbugs:HARD_CODE_PASSWORD | Security - Hard coded password | MAJOR | VULNERABILITY |
| findsecbugs:HAZELCAST_SYMMETRIC_ENCRYPTION | Security - Hazelcast symmetric encryption | MAJOR | VULNERABILITY |
| findsecbugs:HTTPONLY_COOKIE | Security - Cookie without the HttpOnly flag | MAJOR | VULNERABILITY |
| findsecbugs:HTTP_PARAMETER_POLLUTION | Security - HTTP Parameter Pollution | MAJOR | VULNERABILITY |
| findsecbugs:HTTP_RESPONSE_SPLITTING | Security - Potential HTTP Response Splitting | INFO | VULNERABILITY |
| findsecbugs:IMPROPER_UNICODE | Security - Improper handling of Unicode transformations | CRITICAL | VULNERABILITY |
| findsecbugs:INSECURE_COOKIE | Security - Cookie without the secure flag | MAJOR | VULNERABILITY |
| findsecbugs:INSECURE_SMTP_SSL | Security - Insecure SMTP SSL connection | MAJOR | VULNERABILITY |
| findsecbugs:JACKSON_UNSAFE_DESERIALIZATION | Security - Unsafe Jackson deserialization configuration | CRITICAL | VULNERABILITY |
| findsecbugs:JAXRS_ENDPOINT | Security - Found JAX-RS REST endpoint | INFO | VULNERABILITY |
| findsecbugs:JAXWS_ENDPOINT | Security - Found JAX-WS SOAP endpoint | INFO | VULNERABILITY |
| findsecbugs:LDAP_ANONYMOUS | Security - Anonymous LDAP bind | MAJOR | VULNERABILITY |
| findsecbugs:LDAP_ENTRY_POISONING | Security - LDAP Entry Poisoning | CRITICAL | VULNERABILITY |
| findsecbugs:LDAP_INJECTION | Security - Potential LDAP Injection | CRITICAL | VULNERABILITY |
| findsecbugs:MALICIOUS_XSLT | Security - A malicious XSLT could be provided | CRITICAL | VULNERABILITY |
| findsecbugs:MODIFICATION_AFTER_VALIDATION | Security - String is modified after validation and not before it | MAJOR | VULNERABILITY |
| findsecbugs:NORMALIZATION_AFTER_VALIDATION | Security - String is normalized after validation and not before it | MAJOR | VULNERABILITY |
| findsecbugs:NULL_CIPHER | Security - NullCipher is insecure | MAJOR | VULNERABILITY |
| findsecbugs:OBJECT_DESERIALIZATION | Security - Object deserialization is used in {1} | CRITICAL | VULNERABILITY |
| findsecbugs:OGNL_INJECTION | Security - Potential code injection when using OGNL expression | CRITICAL | VULNERABILITY |
| findsecbugs:OVERLY_PERMISSIVE_FILE_PERMISSION | Security - Overly permissive file permission | MAJOR | VULNERABILITY |
| findsecbugs:PADDING_ORACLE | Security - Cipher is susceptible to Padding Oracle | MAJOR | VULNERABILITY |
| findsecbugs:PATH_TRAVERSAL_OUT | Security - Potential Path Traversal (file write) | MAJOR | VULNERABILITY |
| findsecbugs:PERMISSIVE_CORS | Security - Overly permissive CORS policy | MAJOR | VULNERABILITY |
| findsecbugs:PLAY_UNVALIDATED_REDIRECT | Security - Unvalidated Redirect (Play Framework) | MAJOR | VULNERABILITY |
| findsecbugs:PREDICTABLE_RANDOM | Security - Predictable pseudorandom number generator | MAJOR | VULNERABILITY |
| findsecbugs:REDOS | Security - Regex DOS (ReDOS) | MAJOR | VULNERABILITY |
| findsecbugs:REQUESTDISPATCHER_FILE_DISCLOSURE | Security - RequestDispatcher File Disclosure | MAJOR | VULNERABILITY |
| findsecbugs:RPC_ENABLED_EXTENSIONS | Security - Enabling extensions in Apache XML RPC server or client. | CRITICAL | VULNERABILITY |
| findsecbugs:RSA_KEY_SIZE | Security - RSA usage with short key | MAJOR | VULNERABILITY |
| findsecbugs:RSA_NO_PADDING | Security - RSA with no padding is insecure | MAJOR | VULNERABILITY |
| findsecbugs:SAML_IGNORE_COMMENTS | Security - Ignoring XML comments in SAML may lead to authentication bypass | CRITICAL | VULNERABILITY |
| findsecbugs:SCRIPT_ENGINE_INJECTION | Security - Potential code injection when using Script Engine | CRITICAL | VULNERABILITY |
| findsecbugs:SEAM_LOG_INJECTION | Security - Potential code injection in Seam logging call | CRITICAL | VULNERABILITY |
| findsecbugs:SERVLET_CONTENT_TYPE | Security - Untrusted Content-Type header | INFO | VULNERABILITY |
| findsecbugs:SERVLET_HEADER | Security - HTTP headers untrusted | INFO | VULNERABILITY |
| findsecbugs:SERVLET_HEADER_REFERER | Security - Untrusted Referer header | INFO | VULNERABILITY |
| findsecbugs:SERVLET_HEADER_USER_AGENT | Security - Untrusted User-Agent header | INFO | VULNERABILITY |
| findsecbugs:SERVLET_PARAMETER | Security - Untrusted servlet parameter | INFO | VULNERABILITY |
| findsecbugs:SERVLET_QUERY_STRING | Security - Untrusted query string | INFO | VULNERABILITY |
| findsecbugs:SERVLET_SERVER_NAME | Security - Untrusted Hostname header | INFO | VULNERABILITY |
| findsecbugs:SERVLET_SESSION_ID | Security - Untrusted session cookie value | INFO | VULNERABILITY |
| findsecbugs:SMTP_HEADER_INJECTION | Security - SMTP Header Injection | MAJOR | VULNERABILITY |
| findsecbugs:SPEL_INJECTION | Security - Potential code injection when using Spring Expression | CRITICAL | VULNERABILITY |
| findsecbugs:SPRING_CSRF_PROTECTION_DISABLED | Security - Spring CSRF protection disabled | MAJOR | VULNERABILITY |
| findsecbugs:SPRING_CSRF_UNRESTRICTED_REQUEST_MAPPING | Security - Spring CSRF unrestricted RequestMapping | MAJOR | VULNERABILITY |
| findsecbugs:SPRING_ENDPOINT | Security - Found Spring endpoint | INFO | VULNERABILITY |
| findsecbugs:SPRING_FILE_DISCLOSURE | Security - Spring File Disclosure | MAJOR | VULNERABILITY |
| findsecbugs:SPRING_UNVALIDATED_REDIRECT | Security - Spring Unvalidated Redirect | MAJOR | VULNERABILITY |
| findsecbugs:SQL_INJECTION | Security - Potential SQL Injection | CRITICAL | VULNERABILITY |
| findsecbugs:SQL_INJECTION_ANDROID | Security - Potential Android SQL Injection | CRITICAL | VULNERABILITY |
| findsecbugs:SQL_INJECTION_HIBERNATE | Security - Potential SQL/HQL Injection (Hibernate) | CRITICAL | VULNERABILITY |
| findsecbugs:SQL_INJECTION_JDBC | Security - Potential JDBC Injection | CRITICAL | VULNERABILITY |
| findsecbugs:SQL_INJECTION_JDO | Security - Potential SQL/JDOQL Injection (JDO) | CRITICAL | VULNERABILITY |
| findsecbugs:SQL_INJECTION_JPA | Security - Potential SQL/JPQL Injection (JPA) | CRITICAL | VULNERABILITY |
| findsecbugs:SQL_INJECTION_SPRING_JDBC | Security - Potential JDBC Injection (Spring JDBC) | CRITICAL | VULNERABILITY |
| findsecbugs:SQL_INJECTION_TURBINE | Security - Potential SQL Injection with Turbine | CRITICAL | VULNERABILITY |
| findsecbugs:SQL_INJECTION_VERTX | Security - Potential SQL Injection with Vert.x Sql Client | CRITICAL | VULNERABILITY |
| findsecbugs:SSL_CONTEXT | Security - Weak SSLContext | MAJOR | VULNERABILITY |
| findsecbugs:STATIC_IV | Security - Static IV | MAJOR | VULNERABILITY |
| findsecbugs:STRUTS1_ENDPOINT | Security - Found Struts 1 endpoint | INFO | VULNERABILITY |
| findsecbugs:STRUTS2_ENDPOINT | Security - Found Struts 2 endpoint | INFO | VULNERABILITY |
| findsecbugs:STRUTS_FILE_DISCLOSURE | Security - Struts File Disclosure | MAJOR | VULNERABILITY |
| findsecbugs:STRUTS_FORM_VALIDATION | Security - Struts Form without input validation | INFO | VULNERABILITY |
| findsecbugs:TAPESTRY_ENDPOINT | Security - Found Tapestry page | INFO | VULNERABILITY |
| findsecbugs:TDES_USAGE | Security - DESede is insecure | MAJOR | VULNERABILITY |
| findsecbugs:TEMPLATE_INJECTION_FREEMARKER | Security - Potential template injection with Freemarker | CRITICAL | VULNERABILITY |
| findsecbugs:TEMPLATE_INJECTION_PEBBLE | Security - Potential template injection with Pebble | CRITICAL | VULNERABILITY |
| findsecbugs:TEMPLATE_INJECTION_VELOCITY | Security - Potential template injection with Velocity | CRITICAL | VULNERABILITY |
| findsecbugs:TRUST_BOUNDARY_VIOLATION | Security - Trust Boundary Violation | MAJOR | VULNERABILITY |
| findsecbugs:UNENCRYPTED_SERVER_SOCKET | Security - Unencrypted Server Socket | MAJOR | VULNERABILITY |
| findsecbugs:UNENCRYPTED_SOCKET | Security - Unencrypted Socket | MAJOR | VULNERABILITY |
| findsecbugs:UNSAFE_HASH_EQUALS | Security - Unsafe hash equals | MAJOR | VULNERABILITY |
| findsecbugs:UNVALIDATED_REDIRECT | Security - Unvalidated Redirect | MAJOR | VULNERABILITY |
| findsecbugs:URLCONNECTION_SSRF_FD | Security - URLConnection Server-Side Request Forgery (SSRF) and File Disclosure | MAJOR | VULNERABILITY |
| findsecbugs:URL_REWRITING | Security - URL rewriting method | MAJOR | VULNERABILITY |
| findsecbugs:WEAK_FILENAMEUTILS | Security - FilenameUtils not filtering null bytes | INFO | VULNERABILITY |
| findsecbugs:WEAK_HOSTNAME_VERIFIER | Security - HostnameVerifier that accept any signed certificates | MAJOR | VULNERABILITY |
| findsecbugs:WEAK_MESSAGE_DIGEST_MD5 | Security - MD2, MD4 and MD5 are weak hash functions | MAJOR | VULNERABILITY |
| findsecbugs:WEAK_MESSAGE_DIGEST_SHA1 | Security - SHA-1 is a weak hash function | MAJOR | VULNERABILITY |
| findsecbugs:WEAK_TRUST_MANAGER | Security - TrustManager that accept any certificates | MAJOR | VULNERABILITY |
| findsecbugs:WICKET_ENDPOINT | Security - Found Wicket WebPage | INFO | VULNERABILITY |
| findsecbugs:WICKET_XSS1 | Security - Disabling HTML escaping put the application at risk for XSS | MAJOR | VULNERABILITY |
| findsecbugs:XML_DECODER | Security - XMLDecoder usage | CRITICAL | VULNERABILITY |
| findsecbugs:XPATH_INJECTION | Security - Potential XPath Injection | CRITICAL | VULNERABILITY |
| findsecbugs:XSS_REQUEST_WRAPPER | Security - XSSRequestWrapper is a weak XSS protection | MAJOR | VULNERABILITY |
| findsecbugs:XSS_SERVLET | Security - Potential XSS in Servlet | MAJOR | VULNERABILITY |
| findsecbugs:XXE_DOCUMENT | Security - XML parsing vulnerable to XXE (DocumentBuilder) | CRITICAL | VULNERABILITY |
| findsecbugs:XXE_DTD_TRANSFORM_FACTORY | Security - XML parsing vulnerable to XXE (TransformerFactory) | CRITICAL | VULNERABILITY |
| findsecbugs:XXE_SCHEMA_FACTORY | Security - XML schema processing vulnerable to XXE | CRITICAL | VULNERABILITY |
| findsecbugs:XXE_VALIDATOR | Security - XML validation vulnerable to XXE | CRITICAL | VULNERABILITY |
| findsecbugs:XXE_XMLREADER | Security - XML parsing vulnerable to XXE (XMLReader) | CRITICAL | VULNERABILITY |
| findsecbugs:XXE_XMLSTREAMREADER | Security - XML parsing vulnerable to XXE (XMLStreamReader) | CRITICAL | VULNERABILITY |
| findsecbugs:XXE_XPATH | Security - XML parsing vulnerable to XXE (XPathExpression) | CRITICAL | VULNERABILITY |
| findsecbugs:XXE_XSLT_TRANSFORM_FACTORY | Security - XSLT parsing vulnerable to XXE (TransformerFactory) | CRITICAL | VULNERABILITY |
| java:NoSonar | Track uses of "NOSONAR" comments | MAJOR | CODE_SMELL |
| java:S100 | Method names should comply with a naming convention | MINOR | CODE_SMELL |
| java:S101 | Class names should comply with a naming convention | MINOR | CODE_SMELL |
| java:S103 | Lines should not be too long | MAJOR | CODE_SMELL |
| java:S104 | Files should not have too many lines of code | MAJOR | CODE_SMELL |
| java:S106 | Standard outputs should not be used directly to log anything | MAJOR | CODE_SMELL |
| java:S1065 | Unused labels should be removed | MAJOR | CODE_SMELL |
| java:S1066 | Mergeable "if" statements should be combined | MAJOR | CODE_SMELL |
| java:S1068 | Unused "private" fields should be removed | MAJOR | CODE_SMELL |
| java:S1075 | URIs should not be hardcoded | MINOR | CODE_SMELL |
| java:S108 | Nested blocks of code should not be left empty | MAJOR | CODE_SMELL |
| java:S1104 | Class variable fields should not have public accessibility | MINOR | CODE_SMELL |
| java:S1109 | A close curly brace should be located at the beginning of a line | MINOR | CODE_SMELL |
| java:S1110 | Redundant pairs of parentheses should be removed | MAJOR | CODE_SMELL |
| java:S1111 | The "Object.finalize()" method should not be called | MAJOR | BUG |
| java:S1113 | The "Object.finalize()" method should not be overridden | CRITICAL | CODE_SMELL |
| java:S1116 | Empty statements should be removed | MINOR | CODE_SMELL |
| java:S1117 | Local variables should not shadow class fields | MAJOR | CODE_SMELL |
| java:S1118 | Utility classes should not have public constructors | MAJOR | CODE_SMELL |
| java:S1119 | Labels should not be used | MAJOR | CODE_SMELL |
| java:S112 | Generic exceptions should never be thrown | MAJOR | CODE_SMELL |
| java:S1121 | Assignments should not be made from within sub-expressions | MAJOR | CODE_SMELL |
| java:S1123 | Deprecated elements should have both the annotation and the Javadoc tag | MAJOR | CODE_SMELL |
| java:S1124 | Modifiers should be declared in the correct order | MINOR | CODE_SMELL |
| java:S1125 | Boolean literals should not be redundant | MINOR | CODE_SMELL |
| java:S1126 | Return of boolean expressions should not be wrapped into an "if-then-else" statement | MINOR | CODE_SMELL |
| java:S1128 | Unnecessary imports should be removed | MINOR | CODE_SMELL |
| java:S113 | Files should end with a newline | MINOR | CODE_SMELL |
| java:S1132 | Strings literals should be placed on the left side when checking for equality | MINOR | CODE_SMELL |
| java:S114 | Interface names should comply with a naming convention | MINOR | CODE_SMELL |
| java:S1141 | Try-catch blocks should not be nested | MAJOR | CODE_SMELL |
| java:S1143 | Jump statements should not occur in "finally" blocks | CRITICAL | BUG |
| java:S1144 | Unused "private" methods should be removed | MAJOR | CODE_SMELL |
| java:S1147 | Exit methods should not be called | BLOCKER | CODE_SMELL |
| java:S115 | Constant names should comply with a naming convention | CRITICAL | CODE_SMELL |
| java:S1150 | "Enumeration" should not be implemented | MAJOR | CODE_SMELL |
| java:S1151 | "switch case" clauses should not have too many lines of code | MAJOR | CODE_SMELL |
| java:S1153 | "String.valueOf()" should not be appended to a "String" | MINOR | CODE_SMELL |
| java:S1155 | "Collection.isEmpty()" should be used to test for emptiness | MINOR | CODE_SMELL |
| java:S1157 | Case insensitive string comparisons should be made without intermediate upper or lower casing | MINOR | CODE_SMELL |
| java:S1158 | Primitive wrappers should not be instantiated only for "toString" or "compareTo" calls | MINOR | CODE_SMELL |
| java:S1161 | "@Override" should be used on overriding and implementing methods | MAJOR | CODE_SMELL |
| java:S1163 | Exceptions should not be thrown in finally blocks | CRITICAL | CODE_SMELL |
| java:S1165 | Exception classes should have final fields | MINOR | CODE_SMELL |
| java:S1168 | Empty arrays and collections should be returned instead of null | MAJOR | CODE_SMELL |
| java:S1171 | Only static class initializers should be used | MAJOR | CODE_SMELL |
| java:S1172 | Unused method parameters should be removed | MAJOR | CODE_SMELL |
| java:S1174 | "Object.finalize()" should remain protected (versus public) when overriding | CRITICAL | CODE_SMELL |
| java:S1175 | The signature of "finalize()" should match that of "Object.finalize()" | CRITICAL | BUG |
| java:S1176 | Public types, methods and fields (API) should be documented with Javadoc | MAJOR | CODE_SMELL |
| java:S1181 | Throwable and Error should not be caught | MAJOR | CODE_SMELL |
| java:S1182 | Classes that override "clone" should be "Cloneable" and call "super.clone()" | MINOR | CODE_SMELL |
| java:S1185 | Overriding methods should do more than simply call the same method in the super class | MINOR | CODE_SMELL |
| java:S1188 | Anonymous classes should not have too many lines | MAJOR | CODE_SMELL |
| java:S1190 | Future keywords should not be used as names | BLOCKER | CODE_SMELL |
| java:S1191 | Classes from "sun.*" packages should not be used | MAJOR | CODE_SMELL |
| java:S1193 | Exception types should not be tested using "instanceof" in catch blocks | MAJOR | CODE_SMELL |
| java:S1194 | "java.lang.Error" should not be extended | MAJOR | CODE_SMELL |
| java:S1195 | Array designators "[]" should be located after the type in method signatures | MINOR | CODE_SMELL |
| java:S1197 | Array designators "[]" should be on the type, not the variable | MINOR | CODE_SMELL |
| java:S1199 | Nested code blocks should not be used | MINOR | CODE_SMELL |
| java:S120 | Package names should comply with a naming convention | MINOR | CODE_SMELL |
| java:S1201 | "equals" method overrides should accept "Object" parameters | MAJOR | BUG |
| java:S1206 | "equals(Object obj)" and "hashCode()" should be overridden in pairs | MINOR | BUG |
| java:S121 | Control structures should use curly braces | CRITICAL | CODE_SMELL |
| java:S1210 | "equals(Object obj)" should be overridden along with the "compareTo(T obj)" method | MINOR | CODE_SMELL |
| java:S1214 | Interfaces should not solely consist of constants | CRITICAL | CODE_SMELL |
| java:S1215 | Execution of the Garbage Collector should be triggered only by the JVM | CRITICAL | CODE_SMELL |
| java:S1217 | "Thread.run()" should not be called directly | MAJOR | BUG |
| java:S1219 | "switch" statements should not contain non-case labels | BLOCKER | CODE_SMELL |
| java:S122 | Statements should be on separate lines | MAJOR | CODE_SMELL |
| java:S1220 | The default unnamed package should not be used | MINOR | CODE_SMELL |
| java:S1221 | Methods should not be named "tostring", "hashcode" or "equal" | MAJOR | BUG |
| java:S1223 | Non-constructor methods should not have the same name as the enclosing class | MAJOR | CODE_SMELL |
| java:S1226 | Method parameters, caught exceptions and foreach variables' initial values should not be ignored | MINOR | BUG |
| java:S1264 | A "while" loop should be used instead of a "for" loop | MINOR | CODE_SMELL |
| java:S127 | "for" loop stop conditions should be invariant | MAJOR | CODE_SMELL |
| java:S128 | Switch cases should end with an unconditional "break" statement | BLOCKER | CODE_SMELL |
| java:S1301 | "switch" statements should have at least 3 "case" clauses | MINOR | CODE_SMELL |
| java:S131 | "switch" statements should have "default" clauses | CRITICAL | CODE_SMELL |
| java:S1310 | Track uses of "NOPMD" suppression comments | MINOR | CODE_SMELL |
| java:S1313 | Using hardcoded IP addresses is security-sensitive | MINOR | SECURITY_HOTSPOT |
| java:S1314 | Octal values should not be used | BLOCKER | CODE_SMELL |
| java:S1315 | Track uses of "CHECKSTYLE:OFF" suppression comments | MINOR | CODE_SMELL |
| java:S1317 | "StringBuilder" and "StringBuffer" should not be instantiated with a character | MAJOR | BUG |
| java:S1319 | Declarations should use Java collection interfaces such as "List" rather than specific implementation classes such as "LinkedList" | MINOR | CODE_SMELL |
| java:S134 | Control flow statements "if", "for", "while", "switch" and "try" should not be nested too deeply | CRITICAL | CODE_SMELL |
| java:S138 | Methods should not have too many lines | MAJOR | CODE_SMELL |
| java:S1444 | "public static" fields should be constant | MINOR | CODE_SMELL |
| java:S1449 | String operations should not rely on the default system locale | MINOR | CODE_SMELL |
| java:S1481 | Unused local variables should be removed | MINOR | CODE_SMELL |
| java:S1488 | Local variables should not be declared and then immediately returned or thrown | MINOR | CODE_SMELL |
| java:S1541 | Methods should not be too complex | CRITICAL | CODE_SMELL |
| java:S1596 | "Collections.EMPTY_LIST", "EMPTY_MAP", and "EMPTY_SET" should not be used | MINOR | CODE_SMELL |
| java:S1598 | Package declaration should match source file directory | CRITICAL | CODE_SMELL |
| java:S1604 | Anonymous inner classes containing only one method should become lambdas | MAJOR | CODE_SMELL |
| java:S1607 | JUnit4 @Ignored and JUnit5 @Disabled annotations should be used to disable tests and should provide a rationale | MAJOR | CODE_SMELL |
| java:S1611 | Parentheses should be removed from a single lambda parameter when its type is inferred | MINOR | CODE_SMELL |
| java:S1640 | Maps with keys that are enum values should use the EnumMap implementation | MINOR | CODE_SMELL |
| java:S1641 | Sets with elements that are enum values should be replaced with EnumSet | MINOR | CODE_SMELL |
| java:S1643 | Strings should not be concatenated using '+' in a loop | MINOR | CODE_SMELL |
| java:S1656 | Variables should not be self-assigned | MAJOR | BUG |
| java:S1659 | Multiple variables should not be declared on the same line | MINOR | CODE_SMELL |
| java:S1696 | "NullPointerException" should not be caught | MAJOR | CODE_SMELL |
| java:S1698 | "==" and "!=" should not be used when "equals" is overridden | MINOR | CODE_SMELL |
| java:S1699 | Constructors should only call non-overridable methods | CRITICAL | CODE_SMELL |
| java:S1700 | A field should not duplicate the name of its containing class | MAJOR | CODE_SMELL |
| java:S1710 | Annotation repetitions should not be wrapped | MINOR | CODE_SMELL |
| java:S1711 | Standard functional interfaces should not be redefined | MAJOR | CODE_SMELL |
| java:S1751 | Loops with at most one iteration should be refactored | MAJOR | BUG |
| java:S1764 | Identical expressions should not be used on both sides of a binary operator | MAJOR | BUG |
| java:S1821 | "switch" statements and expressions should not be nested | CRITICAL | CODE_SMELL |
| java:S1844 | "Object.wait" should not be called on objects that implement "java.util.concurrent.locks.Condition" | MAJOR | CODE_SMELL |
| java:S1845 | Methods and field names should not be the same or differ only by capitalization | BLOCKER | CODE_SMELL |
| java:S1849 | "Iterator.hasNext()" should not call "Iterator.next()" | MAJOR | BUG |
| java:S1858 | "toString()" should never be called on a String object | MINOR | CODE_SMELL |
| java:S1860 | Synchronization should not be done on instances of value-based classes | MAJOR | BUG |
| java:S1862 | Related "if/else if" statements should not have the same condition | MAJOR | BUG |
| java:S1871 | Two branches in a conditional structure should not have exactly the same implementation | MAJOR | CODE_SMELL |
| java:S1872 | Classes should not be compared by name | MAJOR | BUG |
| java:S1905 | Redundant casts should not be used | MINOR | CODE_SMELL |
| java:S1939 | Extensions and implementations should not be redundant | MINOR | CODE_SMELL |
| java:S1940 | Boolean checks should not be inverted | MINOR | CODE_SMELL |
| java:S1941 | Variables should not be declared before they are relevant | MINOR | CODE_SMELL |
| java:S1942 | Simple class names should be used | MINOR | CODE_SMELL |
| java:S1943 | Classes and methods that rely on the default system encoding should not be used | MINOR | CODE_SMELL |
| java:S1948 | Fields in a "Serializable" class should either be transient or serializable | CRITICAL | CODE_SMELL |
| java:S1989 | Exceptions should not be thrown from servlet methods | MINOR | VULNERABILITY |
| java:S1994 | "for" loop increment clauses should modify the loops' counters | CRITICAL | CODE_SMELL |
| java:S1996 | Files should contain only one top-level class or interface each | MAJOR | CODE_SMELL |
| java:S2039 | Member variable visibility should be specified | MINOR | CODE_SMELL |
| java:S2053 | Password hashing functions should use an unpredictable salt | CRITICAL | VULNERABILITY |
| java:S2055 | The non-serializable super class of a "Serializable" class should have a no-argument constructor | MINOR | BUG |
| java:S2057 | "Serializable" classes should have a "serialVersionUID" | CRITICAL | CODE_SMELL |
| java:S2059 | "Serializable" inner classes of "Serializable" classes should be static | MINOR | CODE_SMELL |
| java:S2060 | "Externalizable" classes should have no-arguments constructors | MAJOR | BUG |
| java:S2061 | Custom serialization methods should have required signatures | MAJOR | BUG |
| java:S2062 | "readResolve" methods should be inheritable | CRITICAL | CODE_SMELL |
| java:S2063 | Comparators should be "Serializable" | CRITICAL | CODE_SMELL |
| java:S2065 | Fields in non-serializable classes should not be "transient" | MINOR | CODE_SMELL |
| java:S2066 | "Serializable" inner classes of non-serializable outer classes should be "static" | MINOR | BUG |
| java:S2068 | Hard-coded passwords are security-sensitive | BLOCKER | SECURITY_HOTSPOT |
| java:S2077 | Formatting SQL queries is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S2092 | Creating cookies without the "secure" flag is security-sensitive | MINOR | SECURITY_HOTSPOT |
| java:S2093 | Try-with-resources should be used | CRITICAL | CODE_SMELL |
| java:S2094 | Classes should not be empty | MINOR | CODE_SMELL |
| java:S2095 | Resources should be closed | BLOCKER | BUG |
| java:S2096 | "main" should not "throw" anything | BLOCKER | CODE_SMELL |
| java:S2097 | "equals(Object obj)" should test the argument's type | MINOR | BUG |
| java:S2109 | Reflection should not be used to check non-runtime annotations | MAJOR | BUG |
| java:S2110 | Invalid "Date" values should not be used | MAJOR | BUG |
| java:S2111 | "BigDecimal(double)" should not be used | MAJOR | BUG |
| java:S2112 | "URL.hashCode" and "URL.equals" should be avoided | MAJOR | CODE_SMELL |
| java:S2114 | Collections should not be passed as arguments to their own methods | MAJOR | BUG |
| java:S2115 | A secure password should be used when connecting to a database | BLOCKER | VULNERABILITY |
| java:S2116 | "hashCode" and "toString" should not be called on array instances | MAJOR | BUG |
| java:S2118 | "writeObject" argument must implement "Serializable" | MAJOR | BUG |
| java:S2119 | "Random" objects should be reused | CRITICAL | BUG |
| java:S2121 | String operations with predictable outcomes should be avoided | MAJOR | BUG |
| java:S2122 | "ScheduledThreadPoolExecutor" should not have 0 core threads | CRITICAL | BUG |
| java:S2123 | Values should not be uselessly incremented | MAJOR | BUG |
| java:S2127 | "Double.longBitsToDouble" should take "long" as argument | MAJOR | BUG |
| java:S2130 | Parsing should be used to convert "Strings" to primitives | MINOR | CODE_SMELL |
| java:S2133 | Objects should not be created only to invoke "getClass" | MAJOR | CODE_SMELL |
| java:S2134 | Classes extending java.lang.Thread should provide a specific "run" behavior | MAJOR | BUG |
| java:S2139 | Exceptions should be either logged or rethrown but not both | MAJOR | CODE_SMELL |
| java:S2140 | Methods of "Random" that return floating point values should not be used in random integer generation | MINOR | CODE_SMELL |
| java:S2141 | Classes that don't define "hashCode()" should not be used in hashes | MAJOR | BUG |
| java:S2147 | Catches should be combined | MINOR | CODE_SMELL |
| java:S2148 | Underscores should be used to make large numbers readable | MINOR | CODE_SMELL |
| java:S2151 | "runFinalizersOnExit" should not be called | CRITICAL | BUG |
| java:S2153 | Unnecessary boxing and unboxing should be avoided | MINOR | BUG |
| java:S2154 | Dissimilar primitive wrappers should not be used with the ternary operator without explicit casting | MAJOR | BUG |
| java:S2156 | "final" classes should not have "protected" members | MINOR | CODE_SMELL |
| java:S2157 | "Cloneables" should implement "clone" | CRITICAL | CODE_SMELL |
| java:S2159 | Unnecessary equality checks should not be made | MAJOR | BUG |
| java:S2160 | Subclasses that add fields to classes that override "equals" should also override "equals" | MINOR | CODE_SMELL |
| java:S2162 | "equals" methods should be symmetric and work for subclasses | MINOR | BUG |
| java:S2166 | Classes named like "Exception" should extend "Exception" or a subclass | MAJOR | CODE_SMELL |
| java:S2167 | "compareTo" should not return "Integer.MIN_VALUE" | MINOR | BUG |
| java:S2168 | Double-checked locking should not be used | BLOCKER | BUG |
| java:S2175 | Inappropriate "Collection" calls should not be made | MAJOR | BUG |
| java:S2176 | Class names should not shadow interfaces or superclasses | CRITICAL | CODE_SMELL |
| java:S2177 | Child class methods named for parent class methods should be overrides | MAJOR | BUG |
| java:S2178 | Short-circuit logic should be used in boolean contexts | BLOCKER | CODE_SMELL |
| java:S2183 | Ints and longs should not be shifted by zero or more than their number of bits-1 | MINOR | BUG |
| java:S2184 | Math operands should be cast before assignment | MINOR | BUG |
| java:S2185 | Do not perform unnecessary mathematical operations | MAJOR | CODE_SMELL |
| java:S2186 | JUnit assertions should not be used in "run" methods | CRITICAL | CODE_SMELL |
| java:S2187 | TestCases should contain tests | BLOCKER | CODE_SMELL |
| java:S2188 | JUnit test cases should call super methods | BLOCKER | CODE_SMELL |
| java:S2189 | Loops should not be infinite | BLOCKER | BUG |
| java:S2197 | Modulus results should not be checked for direct equality | CRITICAL | CODE_SMELL |
| java:S2200 | "compareTo" results should not be checked for specific values | MINOR | BUG |
| java:S2201 | Return values from functions without side effects should not be ignored | MAJOR | BUG |
| java:S2203 | "collect" should be used with "Streams" instead of "list::add" | MINOR | CODE_SMELL |
| java:S2204 | ".equals()" should not be used to test the values of "Atomic" classes | MAJOR | BUG |
| java:S2208 | Wildcard imports should not be used | CRITICAL | CODE_SMELL |
| java:S2209 | "static" members should be accessed statically | MAJOR | CODE_SMELL |
| java:S2222 | Locks should be released on all paths | CRITICAL | BUG |
| java:S2225 | "toString()" and "clone()" methods should not return null | MAJOR | BUG |
| java:S2226 | Servlets should not have mutable instance fields | MAJOR | BUG |
| java:S2229 | Methods should not call same-class methods with incompatible "@Transactional" values | BLOCKER | BUG |
| java:S2230 | Methods with Spring proxying annotations should be public | MAJOR | BUG |
| java:S2232 | "ResultSet.isLast()" should not be used | MAJOR | CODE_SMELL |
| java:S2234 | Parameters should be passed in the correct order | MAJOR | CODE_SMELL |
| java:S2235 | "IllegalMonitorStateException" should not be caught | CRITICAL | CODE_SMELL |
| java:S2236 | Methods "wait(...)", "notify()" and "notifyAll()" should not be called on Thread instances | BLOCKER | BUG |
| java:S2245 | Using pseudorandom number generators (PRNGs) is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S2251 | A "for" loop update clause should move the counter in the right direction | MAJOR | BUG |
| java:S2252 | Loop conditions should be true at least once | MAJOR | BUG |
| java:S2254 | "HttpServletRequest.getRequestedSessionId()" should not be used | CRITICAL | VULNERABILITY |
| java:S2257 | Using non-standard cryptographic algorithms is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S2259 | Null pointers should not be dereferenced | MAJOR | BUG |
| java:S2272 | "Iterator.next()" methods should throw "NoSuchElementException" | MINOR | BUG |
| java:S2273 | "Object.wait()", "Object.notify()" and "Object.notifyAll()" should only be called from synchronized code | MAJOR | BUG |
| java:S2274 | "Object.wait(...)" and "Condition.await(...)" should be called inside a "while" loop | CRITICAL | CODE_SMELL |
| java:S2275 | Printf-style format strings should not lead to unexpected behavior at runtime | BLOCKER | BUG |
| java:S2276 | "wait(...)" should be used instead of "Thread.sleep(...)" when a lock is held | BLOCKER | BUG |
| java:S2293 | The diamond operator ("<>") should be used | MINOR | CODE_SMELL |
| java:S2308 | "deleteOnExit" should not be used | MAJOR | CODE_SMELL |
| java:S2309 | Files should not be empty | MINOR | CODE_SMELL |
| java:S2326 | Unused type parameters should be removed | MAJOR | CODE_SMELL |
| java:S2333 | Redundant modifiers should not be used | MINOR | CODE_SMELL |
| java:S2386 | Mutable fields should not be "public static" | MINOR | CODE_SMELL |
| java:S2387 | Child class fields should not shadow parent class fields | BLOCKER | CODE_SMELL |
| java:S2388 | Inner class calls to super class methods should be unambiguous | MAJOR | CODE_SMELL |
| java:S2390 | Classes should not access their own subclasses during class initialization | CRITICAL | BUG |
| java:S2437 | Unnecessary bit operations should not be performed | BLOCKER | CODE_SMELL |
| java:S2438 | "Thread" should not be used where a "Runnable" argument is expected | MAJOR | CODE_SMELL |
| java:S2440 | Classes with only "static" methods should not be instantiated | MAJOR | CODE_SMELL |
| java:S2441 | Non-serializable objects should not be stored in "javax.servlet.http.HttpSession" instances | MAJOR | BUG |
| java:S2442 | Synchronizing on a "Lock" object should be avoided | MAJOR | CODE_SMELL |
| java:S2444 | Lazy initialization of "static" fields should be "synchronized" | CRITICAL | CODE_SMELL |
| java:S2445 | Blocks should be synchronized on "private final" fields | MAJOR | BUG |
| java:S2446 | "notifyAll()" should be preferred over "notify()" | MAJOR | BUG |
| java:S2447 | "null" should not be returned from a "Boolean" method | CRITICAL | CODE_SMELL |
| java:S2479 | Whitespace and control characters in literals should be explicit | CRITICAL | CODE_SMELL |
| java:S2583 | Conditionally executed code should be reachable | MAJOR | BUG |
| java:S2589 | Boolean expressions should not be gratuitous | MAJOR | CODE_SMELL |
| java:S2612 | Setting loose POSIX file permissions is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S2629 | "Preconditions" and logging arguments should not require evaluation | MAJOR | CODE_SMELL |
| java:S2637 | "@NonNull" values should not be set to null | MINOR | BUG |
| java:S2638 | Method overrides should not change contracts | CRITICAL | CODE_SMELL |
| java:S2639 | Inappropriate regular expressions should not be used | MAJOR | BUG |
| java:S2674 | The value returned from a stream read should be checked | MINOR | BUG |
| java:S2675 | "readObject" should not be "synchronized" | MAJOR | CODE_SMELL |
| java:S2676 | "Math.abs" and negation should not be used on numbers that could be "MIN_VALUE" | MINOR | BUG |
| java:S2677 | "read" and "readLine" return values should be used | MAJOR | BUG |
| java:S2681 | Multiline blocks should be enclosed in curly braces | MAJOR | CODE_SMELL |
| java:S2689 | Files opened in append mode should not be used with "ObjectOutputStream" | BLOCKER | BUG |
| java:S2692 | "indexOf" checks should not be for positive numbers | CRITICAL | CODE_SMELL |
| java:S2693 | Threads should not be started in constructors | BLOCKER | CODE_SMELL |
| java:S2694 | Inner classes which do not reference their owning classes should be "static" | MAJOR | CODE_SMELL |
| java:S2695 | "PreparedStatement" and "ResultSet" methods should be called with valid indices | BLOCKER | BUG |
| java:S2698 | Test assertions should include messages | MINOR | CODE_SMELL |
| java:S2699 | Tests should include assertions | BLOCKER | CODE_SMELL |
| java:S2701 | Literal boolean values and nulls should not be used in assertions | MINOR | CODE_SMELL |
| java:S2718 | "DateUtils.truncate" from Apache Commons Lang library should not be used | MAJOR | CODE_SMELL |
| java:S2737 | "catch" clauses should do more than rethrow | MINOR | CODE_SMELL |
| java:S2755 | XML parsers should not be vulnerable to XXE attacks | BLOCKER | VULNERABILITY |
| java:S2757 | Non-existent operators like "=+" should not be used | MAJOR | BUG |
| java:S2761 | Unary prefix operators should not be repeated | MAJOR | BUG |
| java:S2786 | Nested "enum"s should not be declared static | MINOR | CODE_SMELL |
| java:S2789 | "null" should not be used with "Optional" | MAJOR | BUG |
| java:S2864 | "entrySet()" should be iterated when both the key and value are needed | MAJOR | CODE_SMELL |
| java:S2885 | Non-thread-safe fields should not be static | MAJOR | BUG |
| java:S2886 | Getters and setters should be synchronized in pairs | MAJOR | BUG |
| java:S2924 | JUnit rules should be used | MINOR | CODE_SMELL |
| java:S2925 | "Thread.sleep" should not be used in tests | MAJOR | CODE_SMELL |
| java:S2970 | Assertions should be complete | BLOCKER | CODE_SMELL |
| java:S2972 | Inner classes should not have too many lines of code | MAJOR | CODE_SMELL |
| java:S2973 | Escaped Unicode characters should not be used | MAJOR | CODE_SMELL |
| java:S2975 | "clone" should not be overridden | BLOCKER | CODE_SMELL |
| java:S3008 | Static non-final field names should comply with a naming convention | MINOR | CODE_SMELL |
| java:S3010 | Static fields should not be updated in constructors | MAJOR | CODE_SMELL |
| java:S3012 | Arrays and lists should not be copied using loops | MINOR | CODE_SMELL |
| java:S3014 | "ThreadGroup" should not be used | BLOCKER | CODE_SMELL |
| java:S3020 | "Collection.toArray()" should be passed an array of the proper type | MINOR | BUG |
| java:S3024 | Arguments to "append" should not be concatenated | MINOR | CODE_SMELL |
| java:S3030 | Classes should not have too many "static" imports | MAJOR | CODE_SMELL |
| java:S3033 | ".isEmpty" should be used to test for the emptiness of StringBuffers/Builders | MINOR | CODE_SMELL |
| java:S3034 | Raw byte values should not be used in bitwise operations in combination with shifts | MAJOR | BUG |
| java:S3038 | Abstract methods should not be redundant | MINOR | CODE_SMELL |
| java:S3039 | Indexes to passed to "String" operations should be within the string's bounds | MAJOR | BUG |
| java:S3042 | "writeObject" should not be the only "synchronized" code in a class | MAJOR | CODE_SMELL |
| java:S3046 | "wait" should not be called when multiple locks are held | BLOCKER | BUG |
| java:S3047 | Multiple loops over the same set should be combined | MINOR | CODE_SMELL |
| java:S3063 | "StringBuilder" data should be used | MAJOR | CODE_SMELL |
| java:S3064 | Assignment of lazy-initialized members should be the last step with double-checked locking | MAJOR | BUG |
| java:S3065 | Min and max used in combination should not always return the same value | MAJOR | BUG |
| java:S3066 | "enum" fields should not be publicly mutable | MINOR | CODE_SMELL |
| java:S3067 | "getClass" should not be used for synchronization | MAJOR | BUG |
| java:S3077 | Non-primitive fields should not be "volatile" | MINOR | BUG |
| java:S3078 | "volatile" variables should not be used with compound operators | MAJOR | BUG |
| java:S3254 | Default annotation parameter values should not be passed as arguments | MINOR | CODE_SMELL |
| java:S3305 | Factory method injection should be used in "@Configuration" classes | CRITICAL | CODE_SMELL |
| java:S3306 | Constructor injection should be used instead of field injection | MAJOR | BUG |
| java:S3329 | Cipher Block Chaining IVs should be unpredictable | CRITICAL | VULNERABILITY |
| java:S3330 | Creating cookies without the "HttpOnly" flag is security-sensitive | MINOR | SECURITY_HOTSPOT |
| java:S3346 | Expressions used in "assert" should not produce side effects | MAJOR | BUG |
| java:S3358 | Ternary operators should not be nested | MAJOR | CODE_SMELL |
| java:S3366 | "this" should not be exposed from constructors | MAJOR | CODE_SMELL |
| java:S3398 | "private" methods called only by inner classes should be moved to those classes | MINOR | CODE_SMELL |
| java:S3414 | Tests should be kept in a dedicated source directory | MAJOR | CODE_SMELL |
| java:S3415 | Assertion arguments should be passed in the correct order | MAJOR | CODE_SMELL |
| java:S3416 | Loggers should be named for their enclosing classes | MINOR | CODE_SMELL |
| java:S3436 | Value-based classes should not be used for locking | MAJOR | BUG |
| java:S3437 | Value-based objects should not be serialized | MINOR | CODE_SMELL |
| java:S3457 | Format strings should be used correctly | MAJOR | CODE_SMELL |
| java:S3518 | Zero should not be a possible denominator | CRITICAL | BUG |
| java:S3551 | Overrides should match their parent class methods in synchronization | MAJOR | BUG |
| java:S3553 | "Optional" should not be used for parameters | MAJOR | CODE_SMELL |
| java:S3577 | Test classes should comply with a naming convention | MINOR | CODE_SMELL |
| java:S3578 | Test methods should comply with a naming convention | MINOR | CODE_SMELL |
| java:S3599 | Double Brace Initialization should not be used | MINOR | BUG |
| java:S3626 | Jump statements should not be redundant | MINOR | CODE_SMELL |
| java:S3631 | "Arrays.stream" should be used for primitive arrays | MAJOR | CODE_SMELL |
| java:S3655 | Optional value should only be accessed after calling isPresent() | MAJOR | BUG |
| java:S3658 | Unit tests should throw exceptions | MINOR | CODE_SMELL |
| java:S3725 | Java 8's "Files.exists" should not be used | MAJOR | CODE_SMELL |
| java:S3749 | Members of Spring components should be injected | CRITICAL | CODE_SMELL |
| java:S3750 | Spring "@Controller" classes should not use "@Scope" | MAJOR | BUG |
| java:S3751 | "@RequestMapping" methods should not be "private" | MAJOR | CODE_SMELL |
| java:S3752 | Allowing both safe and unsafe HTTP methods is security-sensitive | MINOR | SECURITY_HOTSPOT |
| java:S3753 | "@Controller" classes that use "@SessionAttributes" must call "setComplete" on their "SessionStatus" objects | BLOCKER | BUG |
| java:S3776 | Cognitive Complexity of methods should not be too high | CRITICAL | CODE_SMELL |
| java:S3824 | "Map.get" and value test should be replaced with single method call | MAJOR | CODE_SMELL |
| java:S3864 | "Stream.peek" should be used with caution | MAJOR | CODE_SMELL |
| java:S3878 | Arrays should not be created for varargs parameters | MINOR | CODE_SMELL |
| java:S3923 | All branches in a conditional structure should not have exactly the same implementation | MAJOR | BUG |
| java:S3937 | Number patterns should be regular | CRITICAL | CODE_SMELL |
| java:S3958 | Intermediate Stream methods should not be left unused | MAJOR | BUG |
| java:S3959 | Consumed Stream pipelines should not be reused | MAJOR | BUG |
| java:S3972 | Conditionals should start on new lines | CRITICAL | CODE_SMELL |
| java:S3973 | A conditionally executed single line should be denoted by indentation | CRITICAL | CODE_SMELL |
| java:S3981 | Collection sizes and array length comparisons should make sense | MAJOR | BUG |
| java:S3984 | Exceptions should not be created without being thrown | MAJOR | BUG |
| java:S3985 | Unused "private" classes should be removed | MAJOR | CODE_SMELL |
| java:S3986 | Week Year ("YYYY") should not be used for date formatting | MAJOR | BUG |
| java:S4030 | Collection contents should be used | MINOR | CODE_SMELL |
| java:S4032 | Packages containing only "package-info.java" should be removed | MINOR | CODE_SMELL |
| java:S4034 | "Stream" call chains should be simplified when possible | MINOR | CODE_SMELL |
| java:S4036 | Searching OS commands in PATH is security-sensitive | MINOR | SECURITY_HOTSPOT |
| java:S4065 | "ThreadLocal.withInitial" should be preferred | MINOR | CODE_SMELL |
| java:S4087 | "close()" calls should not be redundant | MINOR | CODE_SMELL |
| java:S4143 | Map values should not be replaced unconditionally | MAJOR | BUG |
| java:S4144 | Methods should not have identical implementations | MAJOR | CODE_SMELL |
| java:S4165 | Assignments should not be redundant | MAJOR | CODE_SMELL |
| java:S4201 | Null checks should not be used with "instanceof" | MINOR | CODE_SMELL |
| java:S4266 | "Stream.collect()" calls should not be redundant | MINOR | CODE_SMELL |
| java:S4274 | Asserts should not be used to check the parameters of a public method | MAJOR | CODE_SMELL |
| java:S4275 | Getters and setters should access the expected fields | CRITICAL | BUG |
| java:S4276 | Functional Interfaces should be as specialised as possible | MINOR | CODE_SMELL |
| java:S4288 | Spring components should use constructor injection | MAJOR | CODE_SMELL |
| java:S4347 | Secure random number generators should not output predictable values | CRITICAL | VULNERABILITY |
| java:S4348 | "iterator" should not return "this" | MAJOR | BUG |
| java:S4349 | "write(byte[],int,int)" should be overridden | MINOR | CODE_SMELL |
| java:S4351 | "compareTo" should not be overloaded | MAJOR | BUG |
| java:S4423 | Weak SSL/TLS protocols should not be used | CRITICAL | VULNERABILITY |
| java:S4425 | "Integer.toHexString" should not be used to build hexadecimal strings | MAJOR | CODE_SMELL |
| java:S4426 | Cryptographic keys should be robust | CRITICAL | VULNERABILITY |
| java:S4433 | LDAP connections should be authenticated | CRITICAL | VULNERABILITY |
| java:S4434 | Allowing deserialization of LDAP objects is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S4449 | Nullness of parameters should be guaranteed | MAJOR | CODE_SMELL |
| java:S4454 | "equals" method parameters should not be marked "@Nonnull" | CRITICAL | CODE_SMELL |
| java:S4488 | Composed "@RequestMapping" variants should be preferred | MINOR | CODE_SMELL |
| java:S4502 | Disabling CSRF protections is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S4507 | Delivering code in production with debug features activated is security-sensitive | MINOR | SECURITY_HOTSPOT |
| java:S4512 | Setting JavaBean properties is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S4517 | InputSteam.read() implementation should not return a signed byte | MAJOR | BUG |
| java:S4524 | "default" clauses should be last | CRITICAL | CODE_SMELL |
| java:S4544 | Using unsafe Jackson deserialization configuration is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S4551 | Enum values should be compared with "==" | MAJOR | CODE_SMELL |
| java:S4601 | "HttpSecurity" URL patterns should be correctly ordered | CRITICAL | VULNERABILITY |
| java:S4602 | "@SpringBootApplication" and "@ComponentScan" should not be used in the default package | BLOCKER | BUG |
| java:S4605 | Spring beans should be considered by "@ComponentScan" | CRITICAL | CODE_SMELL |
| java:S4635 | String offset-based methods should be preferred for finding substrings from offsets | CRITICAL | CODE_SMELL |
| java:S4682 | "@CheckForNull" or "@Nullable" should not be used on primitive types | MINOR | CODE_SMELL |
| java:S4684 | Persistent entities should not be used as arguments of "@RequestMapping" methods | CRITICAL | VULNERABILITY |
| java:S4719 | "StandardCharsets" constants should be preferred | MINOR | CODE_SMELL |
| java:S4738 | Java features should be preferred to Guava | MAJOR | CODE_SMELL |
| java:S4790 | Using weak hashing algorithms is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S4830 | Server certificates should be verified during SSL/TLS connections | CRITICAL | VULNERABILITY |
| java:S4838 | An iteration on a Collection should be performed on the type handled by the Collection | MINOR | CODE_SMELL |
| java:S4925 | "Class.forName()" should not load JDBC 4.0+ drivers | MAJOR | CODE_SMELL |
| java:S4929 | "read(byte[],int,int)" should be overridden | MINOR | CODE_SMELL |
| java:S4968 | The upper bound of type variables and wildcards should not be "final" | MINOR | CODE_SMELL |
| java:S4970 | Derived exceptions should not hide their parents' catch blocks | CRITICAL | CODE_SMELL |
| java:S4973 | Strings and Boxed types should be compared using "equals()" | MAJOR | BUG |
| java:S4977 | Type parameters should not shadow other type parameters | MINOR | CODE_SMELL |
| java:S5042 | Expanding archive files without controlling resource consumption is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S5122 | Having a permissive Cross-Origin Resource Sharing policy is security-sensitive | MINOR | SECURITY_HOTSPOT |
| java:S5128 | "Bean Validation" (JSR 380) should be properly configured | CRITICAL | CODE_SMELL |
| java:S5164 | "ThreadLocal" variables should be cleaned up when no longer used | MAJOR | BUG |
| java:S5194 | Use Java 14 "switch" expression | MINOR | CODE_SMELL |
| java:S5247 | Disabling auto-escaping in template engines is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S5261 | "else" statements should be clearly matched with an "if" | MAJOR | CODE_SMELL |
| java:S5301 | "ActiveMQConnectionFactory" should not be vulnerable to malicious code deserialization | MINOR | VULNERABILITY |
| java:S5320 | Broadcasting intents is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S5322 | Receiving intents is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S5324 | Accessing Android external storage is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S5329 | Collection constructors should not be used as java.util.function.Function | MAJOR | CODE_SMELL |
| java:S5332 | Using clear-text protocols is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S5344 | Passwords should not be stored in plaintext or with a fast hashing algorithm | CRITICAL | VULNERABILITY |
| java:S5361 | "String#replace" should be preferred to "String#replaceAll" | CRITICAL | CODE_SMELL |
| java:S5411 | Avoid using boxed "Boolean" types directly in boolean expressions | MINOR | CODE_SMELL |
| java:S5413 | 'List.remove()' should not be used in ascending 'for' loops | MAJOR | CODE_SMELL |
| java:S5443 | Using publicly writable directories is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S5445 | Insecure temporary file creation methods should not be used | CRITICAL | VULNERABILITY |
| java:S5527 | Server hostnames should be verified during SSL/TLS connections | CRITICAL | VULNERABILITY |
| java:S5547 | Cipher algorithms should be robust | CRITICAL | VULNERABILITY |
| java:S5612 | Lambdas should not have too many lines | MAJOR | CODE_SMELL |
| java:S5659 | JWT should be signed and verified with strong cipher algorithms | CRITICAL | VULNERABILITY |
| java:S5663 | Simple string literal should be used for single line strings | MINOR | CODE_SMELL |
| java:S5664 | Whitespace for text block indent should be consistent | MAJOR | CODE_SMELL |
| java:S5665 | Escape sequences should not be used in text blocks | MINOR | CODE_SMELL |
| java:S5669 | Vararg method arguments should not be confusing | MAJOR | CODE_SMELL |
| java:S5679 | OpenSAML2 should be configured to prevent authentication bypass | MAJOR | VULNERABILITY |
| java:S5689 | Disclosing fingerprints from web application technologies is security-sensitive | MINOR | SECURITY_HOTSPOT |
| java:S5693 | Allowing requests with excessive content length is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S5738 | "@Deprecated" code marked for removal should never be used | MAJOR | CODE_SMELL |
| java:S5776 | Exception testing via JUnit ExpectedException rule should not be mixed with other assertions | MAJOR | CODE_SMELL |
| java:S5777 | Exception testing via JUnit @Test annotation should be avoided | MINOR | CODE_SMELL |
| java:S5778 | Only one method invocation is expected when testing runtime exceptions | MAJOR | CODE_SMELL |
| java:S5779 | Assertion methods should not be used within the try block of a try-catch catching an Error | CRITICAL | BUG |
| java:S5783 | Only one method invocation is expected when testing checked exceptions | CRITICAL | BUG |
| java:S5785 | JUnit assertTrue/assertFalse should be simplified to the corresponding dedicated assertion | MAJOR | CODE_SMELL |
| java:S5786 | JUnit5 test classes and methods should have default package visibility | INFO | CODE_SMELL |
| java:S5790 | JUnit5 inner test classes should be annotated with @Nested | CRITICAL | BUG |
| java:S5793 | Migrate your tests from JUnit4 to the new JUnit5 annotations | INFO | CODE_SMELL |
| java:S5803 | Class members annotated with "@VisibleForTesting" should not be accessed from production code | CRITICAL | CODE_SMELL |
| java:S5804 | Allowing user enumeration is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S5808 | Authorizations should be based on strong decisions | MAJOR | VULNERABILITY |
| java:S5810 | JUnit5 test classes and methods should not be silently ignored | MAJOR | BUG |
| java:S5826 | Methods setUp() and tearDown() should be correctly annotated starting with JUnit4 | CRITICAL | CODE_SMELL |
| java:S5831 | AssertJ configuration should be applied | MAJOR | BUG |
| java:S5833 | AssertJ methods setting the assertion context should come before an assertion | MAJOR | BUG |
| java:S5838 | Chained AssertJ assertions should be simplified to the corresponding dedicated assertion | MINOR | CODE_SMELL |
| java:S5841 | AssertJ assertions "allMatch" and "doesNotContains" should also test for emptiness | MINOR | BUG |
| java:S5842 | Repeated patterns in regular expressions should not match the empty string | MINOR | BUG |
| java:S5843 | Regular expressions should not be too complicated | MAJOR | CODE_SMELL |
| java:S5845 | Assertions comparing incompatible types should not be made | CRITICAL | BUG |
| java:S5846 | Empty lines should not be tested with regex MULTILINE flag | CRITICAL | CODE_SMELL |
| java:S5850 | Alternatives in regular expressions should be grouped when used with anchors | MAJOR | BUG |
| java:S5852 | Using slow regular expressions is security-sensitive | CRITICAL | SECURITY_HOTSPOT |
| java:S5853 | Consecutive AssertJ "assertThat" statements should be chained | MINOR | CODE_SMELL |
| java:S5854 | Regexes containing characters subject to normalization should use the CANON_EQ flag | MAJOR | CODE_SMELL |
| java:S5855 | Regex alternatives should not be redundant | MAJOR | BUG |
| java:S5856 | Regular expressions should be syntactically valid | CRITICAL | BUG |
| java:S5857 | Character classes should be preferred over reluctant quantifiers in regular expressions | MINOR | CODE_SMELL |
| java:S5860 | Names of regular expressions named groups should be used | MAJOR | CODE_SMELL |
| java:S5863 | Assertions should not compare an object to itself | MAJOR | BUG |
| java:S5866 | Case insensitive Unicode regular expressions should enable the "UNICODE_CASE" flag | MAJOR | BUG |
| java:S5868 | Unicode Grapheme Clusters should be avoided inside regex character classes | MAJOR | BUG |
| java:S5869 | Character classes in regular expressions should not contain the same character twice | MAJOR | CODE_SMELL |
| java:S5876 | A new session should be created during user authentication | CRITICAL | VULNERABILITY |
| java:S5917 | DateTimeFormatters should not use mismatched year and week numbers | MAJOR | BUG |
| java:S5958 | AssertJ "assertThatThrownBy" should not be used alone | MAJOR | CODE_SMELL |
| java:S5960 | Assertions should not be used in production code | MAJOR | BUG |
| java:S5961 | Test methods should not contain too many assertions | MAJOR | CODE_SMELL |
| java:S5967 | Tests method should not be annotated with competing annotations | MAJOR | BUG |
| java:S5969 | Mocking all non-private methods of a class should be avoided | CRITICAL | CODE_SMELL |
| java:S5970 | Spring's ModelAndViewAssert assertions should be used instead of other assertions | MAJOR | CODE_SMELL |
| java:S5973 | Tests should be stable | MAJOR | CODE_SMELL |
| java:S5976 | Similar tests should be grouped in a single Parameterized test | MAJOR | CODE_SMELL |
| java:S5977 | Tests should use fixed data instead of randomized data | MAJOR | CODE_SMELL |
| java:S5979 | Annotated Mockito objects should be initialized | BLOCKER | BUG |
| java:S5993 | Constructors of an "abstract" class should not be declared "public" | MAJOR | CODE_SMELL |
| java:S5994 | Regex patterns following a possessive quantifier should not always fail | CRITICAL | BUG |
| java:S5996 | Regex boundaries should not be used in a way that can never be matched | CRITICAL | BUG |
| java:S5998 | Regular expressions should not overflow the stack | MAJOR | BUG |
| java:S6001 | Back references in regular expressions should only refer to capturing groups that are matched before the reference | CRITICAL | BUG |
| java:S6002 | Regex lookahead assertions should not be contradictory | CRITICAL | BUG |
| java:S6019 | Reluctant quantifiers in regular expressions should be followed by an expression that can't match the empty string | MAJOR | CODE_SMELL |
| java:S6035 | Single-character alternations in regular expressions should be replaced with character classes | MAJOR | CODE_SMELL |
| java:S6068 | Call to Mockito method "verify", "when" or "given" should be simplified | MINOR | CODE_SMELL |
| java:S6070 | The regex escape sequence \cX should only be used with characters in the @-_ range | MAJOR | BUG |
| java:S6073 | Mockito argument matchers should be used on all parameters | MAJOR | BUG |
| java:S6103 | AssertJ assertions with "Consumer" arguments should contain assertion inside consumers | MAJOR | BUG |
| java:S6104 | Map "computeIfAbsent()" and "computeIfPresent()" should not be used to add "null" values. | CRITICAL | BUG |
| java:S6126 | String multiline concatenation should be replaced with Text Blocks | MAJOR | CODE_SMELL |
| java:S6201 | Pattern Matching for "instanceof" operator should be used instead of simple "instanceof" + cast | MINOR | CODE_SMELL |
| java:S6202 | Operator "instanceof" should be used instead of "A.class.isInstance()" | MAJOR | CODE_SMELL |
| java:S6203 | Text blocks should not be used in complex expressions | MINOR | CODE_SMELL |
| java:S6205 | Switch arrow labels should not use redundant keywords | MINOR | CODE_SMELL |
| java:S6207 | Redundant constructors/methods should be avoided in records | MAJOR | CODE_SMELL |
| java:S6208 | Comma-separated labels should be used in Switch with colon case | INFO | CODE_SMELL |
| java:S6209 | Members ignored during record serialization should not be used | CRITICAL | BUG |
| java:S6211 | Custom getter method should not be used to override record's getter behavior | MAJOR | CODE_SMELL |
| java:S6213 | Restricted Identifiers should not be used as Identifiers | MAJOR | CODE_SMELL |
| java:S6216 | Reflection should not be used to increase accessibility of records' fields | MAJOR | BUG |
| java:S6217 | Permitted types of a sealed class should be omitted if they are declared in the same file | MINOR | CODE_SMELL |
| java:S6219 | 'serialVersionUID' field should not be set to '0L' in records | MINOR | CODE_SMELL |
| java:S6241 | Region should be set explicitly when creating a new "AwsClient" | MAJOR | CODE_SMELL |
| java:S6242 | Credentials Provider should be set explicitly when creating a new "AwsClient" | MAJOR | CODE_SMELL |
| java:S6243 | Reusable resources should be initialized at construction time of Lambda functions | MAJOR | CODE_SMELL |
| java:S6244 | Consumer Builders should be used | MINOR | CODE_SMELL |
| java:S6246 | Lambdas should not invoke other lambdas synchronously | MINOR | CODE_SMELL |
| java:S6262 | AWS region should not be set with a hardcoded String | MINOR | CODE_SMELL |
| java:S6263 | Using long-term access keys is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S6288 | Authorizing non-authenticated users to use keys in the Android KeyStore is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S6293 | Using biometric authentication without a cryptographic solution is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S6301 | Mobile database encryption keys should not be disclosed | MAJOR | VULNERABILITY |
| java:S6326 | Regular expressions should not contain multiple spaces | MAJOR | CODE_SMELL |
| java:S6331 | Regular expressions should not contain empty groups | MAJOR | CODE_SMELL |
| java:S6353 | Regular expression quantifiers and character classes should be used concisely | MINOR | CODE_SMELL |
| java:S6355 | Deprecated annotations should include explanations | MAJOR | CODE_SMELL |
| java:S6362 | Enabling JavaScript support for WebViews is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S6363 | Enabling file access for WebViews is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S6373 | XML parsers should not allow inclusion of arbitrary files | BLOCKER | VULNERABILITY |
| java:S6376 | XML parsers should not be vulnerable to Denial of Service attacks | MAJOR | VULNERABILITY |
| java:S6377 | XML signatures should be validated securely | MAJOR | VULNERABILITY |
| java:S6395 | Non-capturing groups without quantifier should not be used | MAJOR | CODE_SMELL |
| java:S6396 | Superfluous curly brace quantifiers should be avoided | MAJOR | CODE_SMELL |
| java:S6397 | Character classes in regular expressions should not contain only one character | MAJOR | CODE_SMELL |
| java:S6418 | Hard-coded secrets are security-sensitive | BLOCKER | SECURITY_HOTSPOT |
| java:S6432 | Counter Mode initialization vectors should not be reused | CRITICAL | VULNERABILITY |
| java:S6437 | Credentials should not be hard-coded | BLOCKER | VULNERABILITY |
| java:S6485 | Hash-based collections with known capacity should be initialized with the proper related static method. | MAJOR | CODE_SMELL |
| java:S6539 | Classes should not depend on an excessive number of classes (aka Monster Class) | INFO | CODE_SMELL |
| java:S6665 | Redundant nullability annotations should be removed | MAJOR | CODE_SMELL |
| java:S6804 | "@Value" annotation should inject property or SpEL expression | MAJOR | CODE_SMELL |
| java:S6806 | Model attributes should follow the Java identifier naming convention | MAJOR | BUG |
| java:S6809 | Methods with Spring proxy should not be called via "this" | CRITICAL | CODE_SMELL |
| java:S6810 | Async methods should return void or Future | MAJOR | BUG |
| java:S6813 | Field dependency injection should be avoided | MAJOR | CODE_SMELL |
| java:S6814 | Optional REST parameters should have an object type | CRITICAL | CODE_SMELL |
| java:S6816 | Nullable injected fields and parameters should provide a default value | CRITICAL | BUG |
| java:S6817 | Use of the "@Async" annotation on methods declared within a "@Configuration" class in Spring Boot | CRITICAL | BUG |
| java:S6818 | "@Autowired" should only be used on a single constructor | CRITICAL | BUG |
| java:S6829 | "@Autowired" should be used when multiple constructors are provided | MAJOR | CODE_SMELL |
| java:S6830 | Bean names should adhere to the naming conventions | MAJOR | CODE_SMELL |
| java:S6831 | "@Qualifier" should not be used on "@Bean" methods | MAJOR | BUG |
| java:S6832 | Non-singleton Spring beans should not be injected into singleton beans | MAJOR | CODE_SMELL |
| java:S6833 | "@Controller" should be replaced with "@RestController" | MAJOR | CODE_SMELL |
| java:S6837 | Superfluous "@ResponseBody" annotations should be removed | MAJOR | CODE_SMELL |
| java:S6838 | "@Bean" methods for Singleton should not be invoked in "@Configuration" when proxyBeanMethods is false | MAJOR | BUG |
| java:S6856 | "@PathVariable" annotation should be present if a path variable is used | MAJOR | BUG |
| java:S6857 | SpEL expression should have a valid syntax | CRITICAL | BUG |
| java:S6862 | Beans in "@Configuration" class should have different names | MAJOR | BUG |
| java:S6863 | Set appropriate Status Codes on HTTP responses | MAJOR | BUG |
| java:S6876 | Reverse iteration should utilize reversed view | CRITICAL | CODE_SMELL |
| java:S6877 | Reverse view should be used instead of reverse copy in read-only cases | CRITICAL | CODE_SMELL |
| java:S6878 | Use record pattern instead of explicit field access | MAJOR | CODE_SMELL |
| java:S6880 | Use switch instead of if-else chain to compare a variable against multiple cases | MAJOR | CODE_SMELL |
| java:S6881 | Virtual threads should be used for tasks that include heavy blocking operations | CRITICAL | BUG |
| java:S6885 | Use built-in "Math.clamp" methods | MAJOR | CODE_SMELL |
| java:S6889 | Proper Sensor Resource Management | MAJOR | CODE_SMELL |
| java:S6891 | Exact alarms should not be abused | MAJOR | CODE_SMELL |
| java:S6898 | High frame rates should not be used | MAJOR | CODE_SMELL |
| java:S6901 | "setDaemon", "setPriority" and "getThreadGroup" should not be invoked on virtual threads | MAJOR | BUG |
| java:S6904 | Avoid using "FetchType.EAGER" | MAJOR | CODE_SMELL |
| java:S6905 | SQL queries should retrieve only necessary fields | MAJOR | CODE_SMELL |
| java:S6906 | Virtual threads should not run tasks that include synchronized code | MAJOR | BUG |
| java:S6909 | Constant parameters in a "PreparedStatement" should not be set more than once | MAJOR | CODE_SMELL |
| java:S6912 | Use batch Processing in JDBC | MAJOR | CODE_SMELL |
| java:S6913 | "Math.clamp" should be used with correct ranges | MAJOR | BUG |
| java:S6914 | Use Fused Location to optimize battery power | MAJOR | CODE_SMELL |
| java:S6915 | "String.indexOf" should be used with correct ranges | MAJOR | BUG |
| java:S6916 | Use when instead of a single if inside a pattern match body | MAJOR | CODE_SMELL |
| java:S6923 | Motion Sensor should not use gyroscope | MAJOR | CODE_SMELL |
| java:S6926 | Bluetooth should be configured to use low power | MAJOR | CODE_SMELL |
| java:S7158 | "String.isEmpty()" should be used to test for emptiness | MINOR | CODE_SMELL |
| java:S7177 | Use appropriate @DirtiesContext modes | MAJOR | CODE_SMELL |
| java:S7178 | Injecting data into static fields is not supported by Spring | CRITICAL | CODE_SMELL |
| java:S7179 | @Cacheable and @CachePut should not be combined | MAJOR | CODE_SMELL |
| java:S7180 | "@Cache*" annotations should only be applied on concrete classes | MAJOR | CODE_SMELL |
| java:S7183 | @InitBinder methods should have void return type | MAJOR | CODE_SMELL |
| java:S7184 | "@Scheduled" annotation should only be applied to no-arg methods | CRITICAL | BUG |
| java:S7185 | @EventListener methods should have one parameter at most | CRITICAL | BUG |
| java:S7186 | Methods returning "Page" or "Slice" must take "Pageable" as an input parameter | CRITICAL | CODE_SMELL |
| java:S7190 | Methods annotated with "@BeforeTransaction" or "@AfterTransaction" must respect the contract | CRITICAL | CODE_SMELL |
| java:S7409 | Exposing native code through JavaScript interfaces is security-sensitive | MAJOR | SECURITY_HOTSPOT |
| java:S7435 | Processing persistent unique identifiers is security-sensitive | MINOR | SECURITY_HOTSPOT |
| java:S7466 | Unnamed variable declarations should use the "var" identifier | MINOR | CODE_SMELL |
| java:S7467 | Unused exception parameter should use the unnamed variable pattern | MINOR | CODE_SMELL |
| java:S7475 | Types of unused record components should be removed from pattern matching | INFO | CODE_SMELL |
| java:S7476 | Comments should start with the appropriate number of slashes | MINOR | CODE_SMELL |
| java:S864 | Limited dependence should be placed on operator precedence | MAJOR | CODE_SMELL |
| java:S888 | Equality operators should not be used in "for" loop termination conditions | CRITICAL | CODE_SMELL |
| java:S899 | Return values should not be ignored when they contain the operation status code | MINOR | BUG |

**Count:** 1462
