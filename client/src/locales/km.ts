import type en from "./en";

export default {
  common: {
    cancel: "បោះបង់",
    loading: "កំពុងផ្ទុក...",
  },
  language: {
    label: "ភាសា",
    en: "English",
    km: "ខ្មែរ",
  },
  sidebar: {
    dashboard: "ផ្ទាំងគ្រប់គ្រង",
    stations: "ស្ថានីយ៍",
    transactions: "ប្រតិបត្តិការ",
    staff: "បុគ្គលិក",
    settings: "ការកំណត់",
    soon: "ឆាប់ៗនេះ",
    logout: "ចាកចេញ",
  },
  auth: {
    brand: {
      eyebrow: "កុងសូលប្រតិបត្តិការ",
      titleLine1: "ស្ថានីយ៍ប្រេងឥន្ធនៈ",
      titleLine2: "ការគ្រប់គ្រង",
      tagline:
        "ភាពមើលឃើញភ្លាមៗលើគ្រប់ម៉ាស៊ីនបូម ធុងសាំង និងស្ថានីយ៍ — ចាប់ពីវេនការងារដំបូងរហូតដល់វេនចុងក្រោយ។",
      statusOnline: "ស្ថានភាព៖ អនឡាញ",
      mobileTitle: "ការគ្រប់គ្រងស្ថានីយ៍ប្រេងឥន្ធនៈ",
    },
    login: {
      eyebrow: "ការចូលប្រើកុងសូល",
      title: "ចូលប្រើប្រាស់",
      subtitle: "សូមបញ្ចូលព័ត៌មានសម្គាល់ប្រតិបត្តិករដើម្បីបន្ត។",
      username: "ឈ្មោះអ្នកប្រើប្រាស់",
      password: "ពាក្យសម្ងាត់",
      usernameRequired: "តម្រូវឱ្យបញ្ចូលឈ្មោះអ្នកប្រើប្រាស់",
      passwordRequired: "តម្រូវឱ្យបញ្ចូលពាក្យសម្ងាត់",
      submit: "ចូលប្រើប្រាស់",
    },
  },
  dashboard: {
    title: "ផ្ទាំងគ្រប់គ្រង",
    subtitle: "ទិដ្ឋភាពទូទៅនៃស្ថានីយ៍ ម៉ាស៊ីនបូម និងធុងសាំងរបស់អ្នក។",
    stats: {
      activePumps: {
        label: "ម៉ាស៊ីនបូមកំពុងដំណើរការ",
        hint: "មិនទាន់មានការតភ្ជាប់ទិន្នន័យម៉ាស៊ីនបូម",
      },
      fuelInStock: {
        label: "ស្តុកសាំង",
        hint: "មិនទាន់មានការតភ្ជាប់ទិន្នន័យធុងសាំង",
      },
      todaysTransactions: {
        label: "ប្រតិបត្តិការថ្ងៃនេះ",
        hint: "មិនទាន់មានការតភ្ជាប់ប្រព័ន្ធលក់",
      },
      openAlerts: {
        label: "ការជូនដំណឹងសកម្ម",
        hint: "មិនទាន់មានការតភ្ជាប់ប្រព័ន្ធជូនដំណឹង",
      },
    },
  },
  stations: {
    title: "ស្ថានីយ៍",
    subtitle: "គ្រប់គ្រងស្ថានីយ៍ប្រេងឥន្ធនៈរបស់អ្នក។",
    addStation: "បន្ថែមស្ថានីយ៍",
    searchPlaceholder: "ស្វែងរកតាមឈ្មោះ ឬលេខកូដ...",
    noResults: "រកមិនឃើញស្ថានីយ៍ទេ។",
    empty: "មិនទាន់មានស្ថានីយ៍ទេ — សូមបន្ថែមស្ថានីយ៍ដំបូងរបស់អ្នកខាងលើ។",
    deleteConfirm: 'លុបស្ថានីយ៍ "{{name}}" មែនទេ? សកម្មភាពនេះមិនអាចត្រឡប់វិញបានទេ។',
    refreshing: "កំពុងធ្វើបច្ចុប្បន្នភាព...",
    pageInfo: "ទំព័រ {{page}} នៃ {{totalPages}}",
    stationCount_one: "{{count}} ស្ថានីយ៍",
    stationCount_other: "{{count}} ស្ថានីយ៍",
    previous: "មុន",
    next: "បន្ទាប់",
    columns: {
      name: "ឈ្មោះ",
      code: "កូដ",
      address: "អាសយដ្ឋាន",
      phone: "ទូរស័ព្ទ",
      status: "ស្ថានភាព",
    },
    status: {
      enabled: "បើក",
      disabled: "បិទ",
    },
    actions: {
      edit: "កែសម្រួលស្ថានីយ៍",
      delete: "លុបស្ថានីយ៍",
    },
    form: {
      addTitle: "បន្ថែមស្ថានីយ៍",
      editTitle: "កែសម្រួលស្ថានីយ៍",
      addDescription: "ចុះឈ្មោះស្ថានីយ៍ប្រេងឥន្ធនៈថ្មី។",
      editDescription: "កែប្រែព័ត៌មានលម្អិតសម្រាប់ {{code}}។",
      name: "ឈ្មោះ",
      code: "កូដ",
      address: "អាសយដ្ឋាន",
      phone: "ទូរស័ព្ទ",
      latitude: "រយៈទទឹង",
      longitude: "រយៈបណ្តោយ",
      nameRequired: "តម្រូវឱ្យបញ្ចូលឈ្មោះ",
      codeRequired: "តម្រូវឱ្យបញ្ចូលកូដ",
      cancel: "បោះបង់",
      submitAdd: "បន្ថែមស្ថានីយ៍",
      submitSave: "រក្សាទុកការផ្លាស់ប្តូរ",
    },
  },
} satisfies typeof en;
