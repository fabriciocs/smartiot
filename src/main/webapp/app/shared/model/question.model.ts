import { IFeedbackForm } from 'app/shared/model/feedback-form.model';

export interface IQuestion {
  id?: number;
  questionText?: string;
  questionType?: string;
  feedbackForm?: IFeedbackForm | null;
}

export const defaultValue: Readonly<IQuestion> = {};
