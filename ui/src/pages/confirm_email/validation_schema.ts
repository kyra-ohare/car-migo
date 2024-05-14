import * as Yup from 'yup';
import { validation } from '../../constants';

export const confirmEmailValidationSchema = Yup.object().shape({
  email: Yup.string().required(validation.NO_EMPTY_EMAIL),
});
