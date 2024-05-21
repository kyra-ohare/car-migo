export interface IPlatformUserCreation {
  firstName: string;
  lastName: string;
  dob: string;
  email: string;
  password: string;
  confirmPassword: string;
  phoneNumber: string;
  passenger: boolean;
  driver: boolean;
}
export interface IPlatformUserEntity {
  id: number;
  createdDate: string;
  firstName: string;
  lastName: string;
  dob: string;
  email: string;
  phoneNumber: string;
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
