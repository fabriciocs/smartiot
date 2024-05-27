import dayjs from 'dayjs';
import { IFeedbackForm } from 'app/shared/model/feedback-form.model';
import { ISysUser } from 'app/shared/model/sys-user.model';

export interface IFeedbackResponse {
  id?: number;
  responseData?: string;
  submittedAt?: dayjs.Dayjs;
  form?: IFeedbackForm | null;
  user?: ISysUser | null;
}

export const defaultValue: Readonly<IFeedbackResponse> = {};
