export function processUserErrorMsgs(errorMsg: string): {
  firstName: string;
  lastName: string;
  phoneNumber: string;
  email: string;
  password: string;
} {
  const sanitisedErrorMsg: string = errorMsg.substring(
    errorMsg.indexOf(":") + 1
  );
  const arrErrorMsgs: string[] = sanitisedErrorMsg.split(",");

  const dict: {
    firstName: string;
    lastName: string;
    phoneNumber: string;
    email: string;
    password: string;
  } = {
    firstName: "",
    lastName: "",
    phoneNumber: "",
    email: "",
    password: "",
  };

  for (const key of arrErrorMsgs) {
    if (key.startsWith(" firstName")) {
      dict.firstName += key + ",";
    } else if (key.startsWith(" lastName")) {
      dict.lastName += key + ",";
    } else if (key.startsWith(" phoneNumber")) {
      dict.phoneNumber += key + ",";
    } else if (key.startsWith(" email")) {
      dict.email += key + ",";
    } else if (key.startsWith(" password")) {
      dict.password += key + ",";
    }
  }
  dict.firstName = dict.firstName
    .replaceAll("firstName", '"First Name"')
    .replaceAll(",", ".");

  dict.lastName = dict.lastName
    .replaceAll("lastName", '"Last Name"')
    .replaceAll(",", ".");

  dict.phoneNumber = dict.phoneNumber
    .replaceAll("phoneNumber", '"Phone Number"')
    .replaceAll(",", ".");

  dict.email = dict.email.replaceAll("email", '"Email"').replaceAll(",", ".");

  dict.password = dict.password
    .replaceAll("password", '"Password"')
    .replaceAll(",", ".");

  // console.log(dict.firstName);
  // console.log(dict.lastName);
  // console.log(dict.phoneNumber);
  // console.log(dict.email);
  // console.log(dict.password);

  return {
    firstName: dict.firstName,
    lastName: dict.lastName,
    phoneNumber: dict.phoneNumber,
    email: dict.email,
    password: dict.password,
  };
}
