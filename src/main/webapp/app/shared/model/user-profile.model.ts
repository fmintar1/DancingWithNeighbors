import dayjs from 'dayjs';
import { Styles } from 'app/shared/model/enumerations/styles.model';

export interface IUserProfile {
  id?: number;
  name?: string | null;
  birthdate?: string | null;
  location?: string | null;
  styles?: Styles | null;
  availability?: string | null;
  imageContentType?: string | null;
  image?: string | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
