export interface IPlatformUser {
  firstName: string;
  lastName: string;
  dob: string;
  email: string;
  phoneNumber: string;
}

export interface IPlatformUserCreation extends IPlatformUser {
  password: string;
  confirmPassword: string;
  passenger: boolean;
  driver: boolean;
}
export interface IPlatformUserEntity extends IPlatformUser {
  id: number;
  createdDate: string;
  userAccessStatus: {
    id: number;
    status: string;
  };
  passenger: boolean;
  driver: boolean;
}

export interface IPlatformUserEmail {
  email: string;
}
