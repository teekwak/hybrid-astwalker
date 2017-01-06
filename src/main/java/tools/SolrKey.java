package tools;

import java.util.HashMap;
import java.util.Map;

public class SolrKey {
	public static final Map<String, String> mapping = new HashMap<>();
	{
		mapping.put("importsScore", "snippet_imports");
		mapping.put("variableNameScore", "snippet_variable_names");
		mapping.put("classNameScore", "snippet_class_name");
		mapping.put("authorScore", "snippet_author_name");
		mapping.put("projectScore", "snippet_project_name");
		mapping.put("methodCallScore", "snippet_method_invocation_names");
		mapping.put("methodDecScore", "snippet_method_dec_names");
		mapping.put("sizeScore", "snippet_size");
		mapping.put("importNumScore", "snippet_imports_count");
		mapping.put("complexityScore", "snippet_path_complexity_class_sum");
		mapping.put("extendsScore", "snippet_extends");
		mapping.put("packageScore", "snippet_package");
		mapping.put("fieldsScore", "snippet_number_of_fields");
		mapping.put("isGenericScore", "snippet_is_generic");
		mapping.put("isAbstractScore", "snippet_is_abstract");
		mapping.put("isWildCardScore", "snippet_is_wildcard");
		mapping.put("ownerScore", "snippet_project_owner");
	}
}




//	public static final String AUTHOR_AVATAR = "snippet_author_avatar";
//	public static final String AUTHOR_EMAIL = "snippet_author_email";
//	public static final String AUTHOR_IS_SITE_ADMIN = "snippet_author_site_admin";
//	public static final String AUTHOR_NAME = "snippet_author_name";
//	public static final String AUTHOR_TYPE = "snippet_author_type";
//	public static final String EXPAND_ID = "expand_id";
//	public static final String PROJECT_ADDRESS = "snippet_project_address";
//	public static final String PROJECT_DESCRIPTION = "snippet_project_description";
//	public static final String PROJECT_IS_FORK = "snippet_project_is_fork";
//	public static final String PROJECT_NAME = "snippet_project_name";
//	public static final String PROJECT_OWNER = "snippet_project_owner";
//	public static final String PROJECT_OWNER_AVATAR = "snippet_project_owner_avatar";
//	public static final String SNIPPET_ADDRESS = "snippet_address";
//	public static final String SNIPPET_ADDRESS_LOWER_BOUND = "snippet_address_lower_bound";
//	public static final String SNIPPET_ADDRESS_UPPER_BOUND ="snippet_address_upper_bound";
//	public static final String SNIPPET_ALL_AUTHORS = "snippet_all_authors";
//	public static final String SNIPPET_ALL_AUTHOR_AVATARS = "snippet_all_author_avatars";
//	public static final String SNIPPET_ALL_AUTHOR_EMAILS = "snippet_all_author_emails";
//	public static final String SNIPPET_ALL_COMMENTS = "snippet_all_version_comments";
//	public static final String SNIPPET_ALL_DATES = "snippet_all_dates";
//	public static final String SNIPPET_ALL_VERSIONS = "snippet_all_versions";
//	public static final String SNIPPET_AUTHOR_COUNT = "snippet_author_count";
//	public static final String SNIPPET_COMPLEXITY_DENSITY = "snippet_complexity_density";
//	public static final String SNIPPET_CONTAINING_CLASS_ID = "snippet_containing_class_id";
//	public static final String SNIPPET_CONTAINING_CLASS_COMPLEXITY_SUM = "snippet_containing_class_complexity_sum";
//	public static final String SNIPPET_CODE = "snippet_code";
//	public static final String SNIPPET_DELETED_CODE_CHURN = "snippet_deleted_code_churn";
//	public static final String SNIPPET_EXTENDS = "snippet_extends";
//	public static final String SNIPPET_EXTENDS_SHORT = "snippet_extends_short";
//	public static final String SNIPPET_GRANULARITY = "snippet_granularity";
//	public static final String SNIPPET_HAS_JAVA_COMMENTS = "snippet_has_java_comments";
//	public static final String SNIPPET_IMPORTS = "snippet_imports";
//	public static final String SNIPPET_IMPORTS_SHORT = "snippet_imports_short";
//	public static final String SNIPPET_IMPORTS_COUNT = "snippet_imports_count";
//	public static final String SNIPPET_IMPLEMENTS = "snippet_implements";
//	public static final String SNIPPET_IMPLEMENTS_SHORT = "snippet_implements_short";
//	public static final String SNIPPET_INSERTION_CODE_CHURN = "snippet_insertion_code_churn";
//	public static final String SNIPPET_INSERTION_DELETION_CODE_CHURN = "snippet_insertion_deletion_code_churn";
//	public static final String SNIPPET_IS_ABSTRACT = "snippet_is_abstract";
//	public static final String SNIPPET_IS_ANONYMOUS = "snippet_is_anonymous";
//	public static final String SNIPPET_IS_GENERIC = "snippet_is_generic";
//	public static final String SNIPPET_IS_INNERCLASS = "snippet_is_innerClass";
//	public static final String SNIPPET_IS_WILDCARD = "snippet_is_wildcard";
//	public static final String SNIPPET_IS_WILDCARD_BOUNDS = "snippet_wildcard_bounds";
//	public static final String SNIPPET_LAST_UPDATED = "snippet_last_updated";
//	public static final String SNIPPET_METHOD_DEC_NAMES = "snippet_method_dec_names";
//	public static final String SNIPPET_METHOD_INVOCATION_NAMES = "snippet_method_invocation_names";
//	public static final String SNIPPET_NAME = "snippet_class_name";
//	public static final String SNIPPET_NAME_DELIMITED = "snippet_class_name_delimited";
//	public static final String SNIPPET_NUMBER_OF_DELETIONS = "snippet_number_of_deletions";
//	public static final String SNIPPET_NUMBER_OF_FIELDS = "snippet_number_of_fields";
//	public static final String SNIPPET_NUMBER_OF_FUNCTIONS = "snippet_number_of_functions";
//	public static final String SNIPPET_NUMBER_OF_INSERTIONS = "snippet_number_of_insertions";
//	public static final String SNIPPET_NUMBER_OF_LINES = "snippet_number_of_lines";
//	public static final String SNIPPET_PACKAGE = "snippet_package";
//	public static final String SNIPPET_PACKAGE_SHORT = "snippet_package_short";
//	public static final String SNIPPET_PATH_COMPLEXITY_SUM = "snippet_path_complexity_class_sum";
//	public static final String SNIPPET_SIZE = "snippet_size";
//	public static final String SNIPPET_THIS_VERSION = "snippet_this_version";
//	public static final String SNIPPET_TOTAL_DELETIONS = "snippet_total_deletions";
//	public static final String SNIPPET_TOTAL_INSERTIONS = "snippet_total_insertions";
//	public static final String SNIPPET_VARIABLE_TYPES = "snippet_variable_types";
//	public static final String SNIPPET_VARIABLE_TYPES_SHORT = "snippet_variable_types_short";
//	public static final String SNIPPET_VARIABLE_NAMES = "snippet_variable_names";
//	public static final String SNIPPET_VARIABLE_NAMES_DELIMITED = "snippet_variable_names_delimited";
//	public static final String SNIPPET_VERSION_COMMENT = "snippet_version_comment";
//
//	// Method Declaration
//	public static final String SNIPPET_METHOD_DEC_CASE_COUNT = "snippet_method_dec_case_count";
//	public static final String SNIPPET_METHOD_DEC_CATCH_COUNT = "snippet_method_dec_catch_count";
//	public static final String SNIPPET_METHOD_DEC_DECLARING_CLASS = "snippet_method_dec_declaring_class";
//	public static final String SNIPPET_METHOD_DEC_DECLARING_CLASS_SHORT = "snippet_method_dec_declaring_class_short";
//	public static final String SNIPPET_METHOD_DEC_END = "snippet_method_dec_end";
//	public static final String SNIPPET_METHOD_DEC_FOR_COUNT = "snippet_method_dec_for_count";
//	public static final String SNIPPET_METHOD_DEC_IF_COUNT = "snippet_method_dec_if_count";
//	public static final String SNIPPET_METHOD_DEC_IS_ABSTRACT = "snippet_method_dec_is_abstract";
//	public static final String SNIPPET_METHOD_DEC_IS_CONSTRUCTOR = "snippet_method_dec_is_constructor";
//	public static final String SNIPPET_METHOD_DEC_IS_GENERIC = "snippet_method_dec_is_generic";
//	public static final String SNIPPET_METHOD_DEC_IS_GENERIC_TYPE_PARAMS = "snippet_method_dec_is_generic_type_parameters";
//	public static final String SNIPPET_METHOD_DEC_IS_STATIC = "snippet_method_dec_is_static";
//	public static final String SNIPPET_METHOD_DEC_IS_VAR_ARGS = "snippet_method_dec_is_var_args";
//	public static final String SNIPPET_METHOD_DEC_IS_WILDCARD_BOUNDS = "snippet_method_dec_is_wildcard_bounds";
//	public static final String SNIPPET_METHOD_DEC_LOGICAL_COUNT = "snippet_method_dec_logical_count";
//	public static final String SNIPPET_METHOD_DEC_NAME = "snippet_method_dec_name";
//	public static final String SNIPPET_METHOD_DEC_NAME_DELIMITED = "snippet_method_dec_name_delimited";
//	public static final String SNIPPET_METHOD_DEC_NUMBER_OF_LOCAL_VARIABLES = "snippet_method_dec_number_of_local_variables";
//	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES = "snippet_method_dec_parameter_types";
//	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_COUNT = "snippet_method_dec_parameter_types_count";
//	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_PLACE = "snippet_method_dec_parameter_types_place";
//	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT = "snippet_method_dec_parameter_types_short";
//	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_PLACE = "snippet_method_dec_parameter_types_short_place";
//	public static final String SNIPPET_METHOD_DEC_PARAMETER_TYPES_SHORT_COUNT = "snippet_method_dec_parameter_types_short_count";
//	public static final String SNIPPET_METHOD_DEC_PATH_COMPLEXITY = "snippet_method_dec_path_complexity_method";
//	public static final String SNIPPET_METHOD_DEC_RETURN_TYPE = "snippet_method_dec_return_type";
//	public static final String SNIPPET_METHOD_DEC_START = "snippet_method_dec_start";
//	public static final String SNIPPET_METHOD_DEC_TERNARY_COUNT = "snippet_method_dec_ternary_count";
//	public static final String SNIPPET_METHOD_DEC_WHILE_COUNT = "snippet_method_dec_while_count";
//
//	// Method Invocation
//	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES = "snippet_method_invocation_arg_types";
//	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_COUNT = "snippet_method_invocation_arg_types_count";
//	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_PLACE = "snippet_method_invocation_arg_types_place";
//	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT = "snippet_method_invocation_arg_types_short";
//	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_COUNT = "snippet_method_invocation_arg_types_short_count";
//	public static final String SNIPPET_METHOD_INVOCATION_ARG_TYPES_SHORT_PLACE = "snippet_method_invocation_arg_types_short_place";
//	public static final String SNIPPET_METHOD_INVOCATION_ARG_VALUES = "snippet_method_invocation_arg_values";
//	public static final String SNIPPET_METHOD_INVOCATION_CALLING_CLASS = "snippet_method_invocation_calling_class";
//	public static final String SNIPPET_METHOD_INVOCATION_CALLING_CLASS_SHORT = "snippet_method_invocation_calling_class_short";
//	public static final String SNIPPET_METHOD_INVOCATION_DECLARING_CLASS = "snippet_method_invocation_declaring_class";
//	public static final String SNIPPET_METHOD_INVOCATION_DECLARING_CLASS_SHORT = "snippet_method_invocation_declaring_class_short";
//	public static final String SNIPPET_METHOD_INVOCATION_END = "snippet_method_invocation_end";
//	public static final String SNIPPET_METHOD_INVOCATION_NAME = "snippet_method_invocation_name";
//	public static final String SNIPPET_METHOD_INVOCATION_NAME_DELIMITED = "snippet_method_invocation_name_delimited";
//	public static final String SNIPPET_METHOD_INVOCATION_START = "snippet_method_invocation_start";

